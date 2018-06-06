package conduit.model

import conduit.types._
import io.circe.Encoder
import io.circe.derivation.deriveEncoder
import io.scalaland.chimney.dsl._
import org.http4s.Uri

case class User(
    email: Email,
    username: Username,
    token: Option[JwtToken],
    bio: Option[Bio],
    image: Option[Uri]
)

object User {
  implicit val encodeUser: Encoder[User] = wrappedEncoder("user", deriveEncoder)

  def fromDbUser(dbUser: DbUser): User =
    dbUser.into[User].transform
}

case class Profile(
    username: Username,
    bio: Option[Bio],
    image: Option[Uri],
    following: Boolean
)

object Profile {
  implicit val encodeProfile: Encoder[Profile] =
    wrappedEncoder("profile", deriveEncoder)
}
