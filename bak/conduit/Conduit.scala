package conduit

import _root_.io.circe.syntax._
import cats.Monad
import cats.effect.{Effect, Sync}
import cats.implicits._
import conduit.Auth.{AuthAuthenticator, AuthService}
import conduit.model.{DbUser, LoginRequest, RegistrationRequest, User}
import conduit.types.Email
import fs2._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext
import tsec.authentication._
import tsec.common.{VerificationFailed, Verified}
import tsec.passwordhashers.jca.BCrypt

abstract class Conduit[F[_]: Effect](implicit ec: ExecutionContext) extends StreamApp[F] {
  import Conduit._
  implicit val dsl: Http4sDsl[F] = Http4sDsl[F]

  val auth: Auth[F] = new Auth[F]

  val routes: HttpService[F] =
    unauthedRoutes[F](auth.userStore, auth.jwtAuth) <+> auth.handler.liftService(authedRoutes[F])

  val logger: HttpService[F] => HttpService[F] = Logger(logHeaders = true, logBody = true)(_)

  def stream(args: List[String], requestShutdown: F[Unit]): Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .mountService(logger(routes), "/api/")
      .serve

}

object Conduit {

  def verifyLoginRequest[F[_]: Sync](
      maybeDbUser: Option[DbUser],
      password: String,
      authenticator: AuthAuthenticator[F]
  )(implicit dsl: Http4sDsl[F]): F[Response[F]] = {
    import dsl._
    maybeDbUser.fold(Forbidden()) { dbUser =>
      BCrypt.checkpw[F](password, dbUser.hashedPassword).flatMap {
        case VerificationFailed => Forbidden()
        case Verified =>
          (Ok(User.fromDbUser(dbUser).asJson), authenticator.create(dbUser.email))
            .mapN((resp, id) => authenticator.embed(resp, id))
      }
    }
  }

  def unauthedRoutes[F[_]: Sync](
      userStore: BackingStore[F, Email, DbUser],
      authenticator: AuthAuthenticator[F]
  )(implicit dsl: Http4sDsl[F]): HttpService[F] = {
    import dsl._

    HttpService[F] {
      case req @ POST -> Root / "users" / "login" =>
        for {
          loginRequest <- req.decodeJson[LoginRequest]
          maybeDbUser  <- userStore.get(loginRequest.email).value
          resp         <- verifyLoginRequest(maybeDbUser, loginRequest.password, authenticator)
        } yield resp

      case req @ POST -> Root / "users" / "register" =>
        for {
          reg        <- req.decodeJson[RegistrationRequest]
          userExists <- userStore.get(reg.email).isDefined
          resp <- if (userExists) BadRequest("")
          else ???
        } yield resp
    }
  }

  def authedRoutes[F[_]: Monad](implicit dsl: Http4sDsl[F]): AuthService[F] = {
    import dsl._

    TSecAuthService {
      case GET -> Root / "user" asAuthed user => Ok("user " + user.username)
      case PUT -> Root / "user" asAuthed user => Ok("update" + user.username)
    }
  }

}
