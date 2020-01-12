package com.arsvechkarev.messaging.list

import android.util.Log
import core.model.messaging.DialogMessage
import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser
import core.model.messaging.TimeDivider
import core.recycler.DisplayableItem
import core.strings.FORMAT_TIME_DIVIDER
import firebase.utils.randomUid
import firebase.utils.thisUser
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

fun List<DialogMessage>.toDisplayableItems(): MutableList<DisplayableItem> {
  val displayMessages = ArrayList<DisplayableItem>()
  for (i in this.indices) {
    val message = this[i]
    Log.d("Processing", message.toString())
    if (i == 0) {
      val timeThisMsg =
        LocalDateTime.ofEpochSecond(message.timestamp, 0, ZonedDateTime.now().offset)
      val timeDivider = TimeDivider(
        randomUid(), timeThisMsg.format(DateTimeFormatter.ofPattern(FORMAT_TIME_DIVIDER))
      )
      displayMessages.add(timeDivider)
    } else {
      if (i != this.size - 1) {
        val nextMessage = this[i + 1]
        val timeThisMsg =
          LocalDateTime.ofEpochSecond(message.timestamp, 0, ZonedDateTime.now().offset)
        val timeNextMsg =
          LocalDateTime.ofEpochSecond(nextMessage.timestamp, 0, ZonedDateTime.now().offset)
        if (timeThisMsg.toLocalDate() != timeNextMsg.toLocalDate()) {
          val timeDivider = TimeDivider(
            randomUid(), timeThisMsg.format(DateTimeFormatter.ofPattern(FORMAT_TIME_DIVIDER))
          )
          displayMessages.add(timeDivider)
        }
      }
    }
    when (thisUser.uid) {
      message.fromUserId -> displayMessages.add(
        MessageThisUser(randomUid(), message.text, message.timestamp)
      )
      message.toUserId -> displayMessages.add(
        MessageOtherUser(randomUid(), message.text, message.timestamp)
      )
    }
  }
  return displayMessages
}

fun List<DialogMessage>.toDisplayableItemsOld(): MutableList<DisplayableItem> {
  val displayMessages = ArrayList<DisplayableItem>()
  for (i in this.indices) {
    val message = this[i]
    Log.d("Processing", message.toString())
    when (thisUser.uid) {
      message.fromUserId -> displayMessages.add(
        MessageThisUser(randomUid(), message.text, message.timestamp)
      )
      message.toUserId -> displayMessages.add(
        MessageOtherUser(randomUid(), message.text, message.timestamp)
      )
    }
    if (i < this.size - 1) {
      val nextMessage = this[i + 1]
      val timeThisMsg =
        LocalDateTime.ofEpochSecond(message.timestamp, 0, ZonedDateTime.now().offset)
      val timeNextMsg =
        LocalDateTime.ofEpochSecond(nextMessage.timestamp, 0, ZonedDateTime.now().offset)
      if (timeThisMsg.toLocalDate() != timeNextMsg.toLocalDate()) {
        val timeDivider = TimeDivider(
          randomUid(), timeThisMsg.format(DateTimeFormatter.ofPattern(FORMAT_TIME_DIVIDER))
        )
        displayMessages.add(timeDivider)
      }
    } else {
      val timeThisMsg =
        LocalDateTime.ofEpochSecond(message.timestamp, 0, ZonedDateTime.now().offset)
      val timeDivider = TimeDivider(
        randomUid(), timeThisMsg.format(DateTimeFormatter.ofPattern(FORMAT_TIME_DIVIDER))
      )
      displayMessages.add(timeDivider)
    }
  }
  return displayMessages
}