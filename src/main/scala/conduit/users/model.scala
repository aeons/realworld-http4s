package conduit.users

import conduit.implicits._
import conduit.users.types._
import conduit.util.{wrappedDecoder, wrappedEncoder}
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection._
import io.circe._
import io.circe.derivation._
import io.circe.refined._
import io.scalaland.chimney.dsl._
import org.http4s._

case class User(
    id: UserId,
    email: Email,
    username: Username,
    hashedPassword: HashedPassword,
    bio: Option[Bio],
    image: Option[Uri],
)

case class AuthUser(
    email: Email,
    token: JwtToken,
    username: Username,
    bio: Option[Bio],
    image: Option[Uri],
)

object AuthUser {
  def fromUser(user: User, token: JwtToken): AuthUser =
    user.into[AuthUser].withFieldConst(_.token, token).transform

  implicit val encoder: Encoder[AuthUser] =
    wrappedEncoder("user", deriveEncoder[AuthUser])
}

case class Following(id: Long, follower: UserId, followee: UserId)

case class Profile(
    username: Username,
    bio: Option[Bio],
    image: Option[Uri],
    following: Option[Boolean]
)

object Profile {
  def fromUser(user: User, following: Option[Boolean]): Profile =
    user.into[Profile].withFieldConst(_.following, following).transform
}

object requests {
  type Password = Refined[String, MinSize[W.`8`.T]]

  case class CreateUserRequest(username: Username, email: Email, password: Password)
  object CreateUserRequest {
    implicit val decoder: Decoder[CreateUserRequest] =
      wrappedDecoder("user", deriveDecoder[CreateUserRequest])
  }
}
