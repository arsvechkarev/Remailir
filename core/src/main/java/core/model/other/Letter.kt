package core.model.other

import core.recycler.DisplayableItem

data class Letter(
  val digit: String
) : DisplayableItem {
  override val id = digit
  override val type = TYPE_LETTER
}