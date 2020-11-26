package com.arsvechkarev.search.presentation

import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.views.Toolbar

class SearchScreen : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      child<Toolbar>(MatchParent, WrapContent) {
      
      }
    }
  }
}