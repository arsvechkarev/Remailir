package com.arsvechkarev.core.datetime

class EnglishTimeFormatter : TimeFormatter {
  
  override fun formatPublishedDate(stringDate: String): String {
    val date = DateTime.of(stringDate)
    val now = DateTime.now()
    return if (date.dayOfYear < now.dayOfYear) {
      val diff = now.dayOfYear - date.dayOfYear
      when {
        diff == 1 -> "${now.hour + (24 - date.hour)} hours ago"
        diff < 14 -> "$diff days ago"
        diff < 21 -> "2 weeks ago"
        diff < 28 -> "3 weeks ago"
        diff < 365 -> "${date.monthName} ${date.dayOfMonth}"
        else -> "${date.monthValue}/${date.dayOfMonth}/${date.year}"
      }
    } else {
      when {
        date.hour < now.hour -> {
          // Date is more that one hour before now
          val diff = now.hour - date.hour
          if (diff == 1) "1 hour ago" else "$diff hours ago"
        }
        date.minute < now.minute -> {
          val diff = now.minute - date.minute
          if (diff == 1) "1 minute ago" else "$diff minutes ago"
        }
        date.minute == now.minute -> "Just now"
        else -> throw IllegalStateException("Unable to parse: $date")
      }
    }
  }
}