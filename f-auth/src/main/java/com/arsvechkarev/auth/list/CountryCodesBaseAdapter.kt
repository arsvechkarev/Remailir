package com.arsvechkarev.auth.list

import core.model.other.Country
import core.model.other.TYPE_COUNTRY
import core.model.other.TYPE_LETTER
import core.recycler.BaseAdapter

class CountryCodesBaseAdapter(
  clickListener: (Country) -> Unit
) : BaseAdapter() {
  
  init {
    delegates.put(TYPE_COUNTRY, CountryAdapterDelegate(clickListener))
    delegates.put(TYPE_LETTER, LetterAdapterDelegate())
  }
  
}