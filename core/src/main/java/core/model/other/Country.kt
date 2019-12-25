package core.model.other

import core.recycler.DisplayableItem

data class Country(
  val name: String,
  val letters: String,
  val code: Int
) : DisplayableItem {
  override val id = name
  override val type = TYPE_COUNTRY
}
