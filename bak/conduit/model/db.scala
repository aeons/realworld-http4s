package conduit.model

import conduit.types._
import org.http4s.Uri
import tsec.passwordhashers.PasswordHash
import tsec.passwordhashers.jca.BCrypt

object db {
    type WithId[A] = (Long, A)

    object WithId {
        implicit class
    }
}

case class DbUser(
    email: Email,
    username: Username,
    hashedPassword: PasswordHash[BCrypt],
    token: Option[JwtToken],
    bio: Option[Bio],
    image: Option[Uri]
)
