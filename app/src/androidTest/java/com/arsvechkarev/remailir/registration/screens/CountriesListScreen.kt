package com.arsvechkarev.remailir.registration.screens

import android.view.View
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.arsvechkarev.remailir.R
import org.hamcrest.Matcher

class CountriesListScreen : Screen<CountriesListScreen>() {
  
  val imageBack = KImageView { withId(R.id.imageBack) }
  val imageSearch = KImageView { withId(R.id.imageSearch) }
  
  val recyclerCountries = KRecyclerView(
    builder = { withId(R.id.recyclerCountries) },
    itemTypeBuilder = { itemType(::CountriesListScreenItem) }
  )
  
  class CountriesListScreenItem(parent: Matcher<View>) :
    KRecyclerItem<CountriesListScreenItem>(parent) {
    val textCountry = KTextView(parent) { withId(R.id.textCountry) }
    val textCountryCode = KTextView(parent) { withId(R.id.textCountryCode) }
  }
}