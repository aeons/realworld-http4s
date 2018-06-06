package conduit

import io.circe._
import io.estatico.newtype.NewType

package object types {

  type Email = Email.Type
  object Email extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
  }

  type Bio = Bio.Type
  object Bio extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
  }

  type JwtToken = JwtToken.Type
  object JwtToken extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
  }

  type Username = Username.Type
  object Username extends NewType.Default[String] {
    implicit val decoder: Decoder[Type] = deriving
    implicit val encoder: Encoder[Type] = deriving
  }

}
