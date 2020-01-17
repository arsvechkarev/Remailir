package com.arsvechkarev.auth.list

import core.model.other.Country
import core.model.other.TYPE_COUNTRY
import core.model.other.TYPE_LETTER
import core.recycler.BaseListAdapter
import core.recycler.DisplayableItem
import log.log

class CountryAndLettersAdapter(
  clickListener: (Country) -> Unit
) : BaseListAdapter() {
  
  init {
    delegates.put(TYPE_COUNTRY, CountryAdapterDelegate(clickListener))
    delegates.put(TYPE_LETTER, LetterAdapterDelegate())
  }
  
  override fun submitList(list: List<DisplayableItem>?) {
    log { "in adapter submit = ${list?.size}" }
    super.submitList(list)
  }
}