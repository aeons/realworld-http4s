package conduit.algebra

import conduit.model.User
import conduit.types._
import org.http4s.Uri

trait UserAlgebra[F[_]] {
  def login(email: Email, password: String): F[Option[User]]
  def register(email: Email, username: Username, password: String): F[Option[User]]
  def get(email: Email): F[Option[User]]
  def update(
      email: Option[Email],
      username: Option[Username],
      password: Option[String],
      bio: Option[Bio],
      image: Option[Uri],
  ): F[User]
}
