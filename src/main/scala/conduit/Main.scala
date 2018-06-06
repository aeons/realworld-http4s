package conduit

import cats.effect.IO
import scala.concurrent.ExecutionContext.Implicits.global

object Main extends Conduit[IO] with Logging

