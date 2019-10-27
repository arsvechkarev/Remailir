package com.arsvechkarev.core.base

import androidx.annotation.StringRes

class Configs(
  @StringRes val title: Int,
  val isBottomBarVisible: Boolean = true,
  val isDotMenuHidden: Boolean = false,
  val isUpArrowShowed: Boolean = false
)