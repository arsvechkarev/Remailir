package core.extensions

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId


fun currentTimestamp(): Long {
  return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
}