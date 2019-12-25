package com.arsvechkarev.auth.list

import core.model.other.Country
import core.model.other.TYPE_COUNTRY
import core.recycler.BaseAdapter

class CountryCodesAdapter(
  clickListener: (Country) -> Unit
) : BaseAdapter<Country>() {
  
  init {
    delegates.put(TYPE_COUNTRY, CountryAdapterDelegate(clickListener))
  }
  
}