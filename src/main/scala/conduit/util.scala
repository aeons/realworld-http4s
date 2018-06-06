package conduit

import io.circe._

package object util {
  def wrappedEncoder[A](name: String, encoder: Encoder[A]): Encoder[A] =
    Encoder.instance(a => Json.obj(name -> encoder(a)))

  def wrappedDecoder[A](name: String, decoder: Decoder[A]): Decoder[A] =
    Decoder.instance(_.get(name)(decoder))
}
