package core.extensions

import core.model.messaging.DialogMessage
import core.strings.FORMAT_TIME_DIVIDER
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.UUID

fun randomUid(): String = UUID.randomUUID().toString()

fun currentTimestamp(): Long {
  return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
}

val DialogMessage.time: LocalDateTime
  get() = LocalDateTime.ofEpochSecond(timestamp, 0, ZonedDateTime.now().offset)

val LocalDateTime.date: LocalDate
  get() = toLocalDate()

fun LocalDateTime.timeDividerFormat(): String {
  return format(DateTimeFormatter.ofPattern(FORMAT_TIME_DIVIDER))
}