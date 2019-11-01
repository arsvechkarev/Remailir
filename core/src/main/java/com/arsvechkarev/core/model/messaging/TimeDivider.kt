package com.arsvechkarev.core.model.messaging

import com.arsvechkarev.core.recycler.DisplayableItem

data class TimeDivider(
  override val id: String,
  val header: String
) : DisplayableItem {
  override val type: Int = ViewTypeConstants.TIME_DIVIDER
}