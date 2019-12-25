package core.model.messaging

import core.recycler.DisplayableItem

data class TimeDivider(
  override val id: String,
  val header: String
) : DisplayableItem {
  override val type: Int = TYPE_TIME_DIVIDER
}