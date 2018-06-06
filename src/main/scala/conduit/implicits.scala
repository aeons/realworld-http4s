package conduit

import cats.implicits._
import doobie.Meta
import io.circe._
import org.http4s.Uri
import tsec.passwordhashers.PasswordHash

object implicits {

  implicit val conduitMetaForUri: Meta[Uri] =
    Meta[String].xmap(Uri.unsafeFromString, _.renderString)

  implicit def conduitMetaForPasswordHash[A]: Meta[PasswordHash[A]] =
    PasswordHash.subst[A](Meta[String])

  implicit val conduitDecoderForUri: Decoder[Uri] =
    Decoder[String].emap(Uri.fromString(_).leftMap(_ => "Could not decode Uri"))

  implicit val conduitEncoderForUri: Encoder[Uri] =
    Encoder[String].contramap(_.renderString)

}
