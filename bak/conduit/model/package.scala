package conduit

import io.circe.{Decoder, Encoder, Json}
import cats.implicits._
import org.http4s.Uri

package object model {
  implicit val uriDecoder: Decoder[Uri] =
    Decoder[String].emap(Uri.fromString(_).leftMap(_ => "Could not decode Uri"))

  implicit val uriEncoder: Encoder[Uri] = Encoder[String].contramap(_.renderString)

  def wrappedEncoder[A](name: String, encoder: Encoder[A]): Encoder[A] =
    Encoder.instance(a => Json.obj(name -> encoder(a)))

  def wrappedDecoder[A](name: String, decoder: Decoder[A]): Decoder[A] =
    Decoder.instance(_.get(name)(decoder))
}
