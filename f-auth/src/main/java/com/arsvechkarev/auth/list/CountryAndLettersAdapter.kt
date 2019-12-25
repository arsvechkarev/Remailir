package com.arsvechkarev.auth.list

import core.model.other.Country
import core.model.other.TYPE_COUNTRY
import core.model.other.TYPE_LETTER
import core.recycler.BaseAdapter
import core.recycler.DisplayableItem

class CountryAndLettersAdapter(
  clickListener: (Country) -> Unit
) : BaseAdapter<DisplayableItem>() {
  
  init {
    delegates.put(TYPE_COUNTRY, CountryAdapterDelegate(clickListener))
    delegates.put(TYPE_LETTER, LetterAdapterDelegate())
  }
  
}