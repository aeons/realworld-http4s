package conduit

import cats.effect.Effect
import conduit.interpreter.InMemory
import conduit.model.DbUser
import conduit.types.Email
import scala.concurrent.duration._
import tsec.authentication._
import tsec.common.SecureRandomId
import tsec.mac.jca.{HMACSHA256, MacSigningKey}

class Auth[F[_]: Effect] {
  val jwtStore: BackingStore[F, SecureRandomId, AugmentedJWT[HMACSHA256, Email]] =
    InMemory.backingStore[F, SecureRandomId, AugmentedJWT[HMACSHA256, Email]](email =>
      SecureRandomId.coerce(email.identity.repr))

  val userStore: BackingStore[F, Email, DbUser] =
    InMemory.backingStore[F, Email, DbUser](_.email)

  val signingKey: MacSigningKey[HMACSHA256] =
    HMACSHA256.unsafeGenerateKey

  val jwtAuth: JWTAuthenticator[F, Email, DbUser, HMACSHA256] =
    JWTAuthenticator.backed.inBearerToken(
      expiryDuration = 1.day,
      maxIdle = None,
      tokenStore = jwtStore,
      identityStore = userStore,
      signingKey = signingKey
    )

  val handler: SecuredRequestHandler[F, Email, DbUser, AugmentedJWT[HMACSHA256, Email]] =
    SecuredRequestHandler(jwtAuth)
}

object Auth {
  type AuthHandler[F[_]] =
    SecuredRequestHandler[F, Email, DbUser, AugmentedJWT[HMACSHA256, Email]]
  type AuthService[F[_]] =
    TSecAuthService[DbUser, AugmentedJWT[HMACSHA256, Email], F]
  type AuthAuthenticator[F[_]] =
    Authenticator[F, Email, DbUser, AugmentedJWT[HMACSHA256, Email]]
}
