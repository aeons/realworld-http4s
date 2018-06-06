package conduit

import scribe.Logger
import scribe.format._

trait Logging {
  Logger.root
    .clearHandlers()
    .withHandler(
      formatter"$date [$threadNameAbbreviated] $positionAbbreviated - $message$newLine"
    )
    .replace()
}
