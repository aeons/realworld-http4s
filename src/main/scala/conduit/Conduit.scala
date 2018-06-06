package conduit

import cats.effect._
import cats.implicits._
import fs2._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.middleware.Logger
import scala.concurrent.ExecutionContext
import doobie._
import doobie.implicits._
import conduit.users.db.users.queries

abstract class Conduit[F[_]: Effect](implicit ec: ExecutionContext) extends StreamApp[F] {
  implicit val dsl: Http4sDsl[F] = Http4sDsl[F]

  val routes: HttpService[F] =
    users.routes[F]

  val logger: HttpService[F] => HttpService[F] = Logger(logHeaders = true, logBody = true)(_)

  val xa = Transactor.fromDriverManager[F]("org.postgresql.Driver",
                                           "jdbc:postgresql://localhost/realworld",
                                           "postgres",
                                           "")
  import conduit.users.types._
  val program = for {
    user <- queries
      .create(Email("asdf@asdfa.de"), Username("asdfa"), HashedPassword("secret"))
      .transact(xa)
    _ <- Sync[F].delay(println(user))
    user1 <- queries
      .update(
        user.copy(username = Username("horses"), image = Some(Uri.uri("https://www.google.com"))))
      .transact(xa)
    _     <- Sync[F].delay(println(user1))
    user2 <- queries.findByEmail(Email("asdf@asdfa.de")).transact(xa)
    _     <- Sync[F].delay(println(user2))

  } yield ()

  def stream(args: List[String], requestShutdown: F[Unit]): Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .mountService(logger(routes), "/api/")
      .serve

}
