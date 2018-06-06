package conduit

import cats.implicits._
import conduit.users.types._
import cats.effect._
import org.http4s.dsl.Http4sDsl
import org.http4s._
import org.http4s.circe._
// import tsec.authentication._
import io.circe.syntax._
import conduit.users.requests._

package object users {

  def createUserWithToken[F[_]: Sync](
      userAlg: UserAlgebra[F],
      tokenAlg: TokenAlgebra[F]
  ): F[(User, JwtToken)] =
    for {
      user <- userAlg
        .create(Email("email"), Username("username"), "secret")
        .map(_.leftWiden[Throwable])
        .rethrow
      token <- tokenAlg.create(user.id)
    } yield (user, token)

  def routes[F[_]: Sync]()(implicit dsl: Http4sDsl[F]) = {
    unauthedRoutes
  }

  def unauthedRoutes[F[_]: Sync](implicit dsl: Http4sDsl[F]) = {
    import dsl._

    val us: User = null
    val to: JwtToken = JwtToken("asd")

    HttpService[F] {
      // Create new user
      case req @ POST -> Root / "users" =>
        for {
          createUserRequest <- req.decodeJson[CreateUserRequest]
          (user, token) = (us, to)
          resp <- Ok(AuthUser.fromUser(user, token).asJson)
        } yield resp

      // Login
      case POST -> Root / "users" / "login" =>
        ???
    }
  }

  // def authedRoutes[F[_]: Sync](implicit dsl: Http4sDsl[F]): AuthService[F] = {
  //   import dsl._

  //   TSecAuthService {
  //     // Get current user
  //     case GET -> Root / "user" asAuthed user =>
  //       Ok("user " + user.username)

  //     // Update current user
  //     case PUT -> Root / "user" asAuthed user =>
  //       Ok("update" + user.username)
  //   }
  // }

}
