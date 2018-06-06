package conduit.users

import doobie.Meta
import io.circe._
import io.estatico.newtype.NewType
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.BCrypt

object types {
  type HashedPassword = PasswordHash[BCrypt]
  object HashedPassword {
    def apply(pw: String): HashedPassword = PasswordHash[BCrypt](pw)
  }

  type UserId = UserId.Type
  object UserId extends NewType.Default[Long] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
    implicit val meta: Meta[Type]       = deriving
  }

  type Email = Email.Type
  object Email extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
    implicit val meta: Meta[Type]       = deriving
  }

  type Bio = Bio.Type
  object Bio extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
    implicit val meta: Meta[Type]       = deriving
  }

  type JwtToken = JwtToken.Type
  object JwtToken extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
    implicit val meta: Meta[Type]       = deriving
  }

  type Username = Username.Type
  object Username extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
    implicit val meta: Meta[Type]       = deriving
  }
}
