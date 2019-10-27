package com.arsvechkarev.core.base

import androidx.annotation.StringRes

class Configurations(
  val addToBackStack: Boolean = false,
  val hideBottomNavigation: Boolean = false,
  val hideDotMenu: Boolean = false,
  val showUpArrow: Boolean = false,
  @StringRes val title: Int = Int.MAX_VALUE
)