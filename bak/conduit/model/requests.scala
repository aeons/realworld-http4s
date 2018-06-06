package conduit.model

import conduit.types._
import io.circe.Decoder
import io.circe.derivation.deriveDecoder

case class LoginRequest(
    email: Email,
    password: String
)

object LoginRequest {
  implicit val loginRequestDecoder: Decoder[LoginRequest] =
    wrappedDecoder("user", deriveDecoder)
}

case class RegistrationRequest(
    email: Email,
    username: Username,
    password: String
)

object RegistrationRequest {
  implicit val registrationRequestDecoder: Decoder[RegistrationRequest] =
    wrappedDecoder("user", deriveDecoder)
}
