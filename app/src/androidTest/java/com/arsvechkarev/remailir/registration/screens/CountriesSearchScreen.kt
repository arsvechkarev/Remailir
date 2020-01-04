package com.arsvechkarev.remailir.registration.screens

import android.view.View
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.arsvechkarev.remailir.R
import org.hamcrest.Matcher

class CountriesSearchScreen : Screen<CountriesSearchScreen>() {
  
  val imageBack = KImageView { withId(R.id.imageBack) }
  val imageCross = KImageView { withId(R.id.imageCross) }
  
  val recyclerSearchedCountries = KRecyclerView(
    builder = { withId(R.id.recyclerCountries) },
    itemTypeBuilder = { itemType(::CountriesSearchScreenItem) }
  )
  
  class CountriesSearchScreenItem(parent: Matcher<View>) :
    KRecyclerItem<CountriesSearchScreenItem>(parent) {
    val textCountry = KTextView(parent) { withId(R.id.textCountry) }
  }
}