package conduit.model

import conduit.types._
import org.http4s.Uri
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.BCrypt

case class DbUser(
    email: Email,
    username: Username,
    hashedPassword: PasswordHash[BCrypt],
    token: Option[JwtToken],
    bio: Option[Bio],
    image: Option[Uri]
)
