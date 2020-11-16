package com.arsvechkarev.core.viewbuilding

import com.arsvechkarev.viewdsl.Ints.dp

object Dimens {
  
  val DividerHeight get() = 1.dp
  
  val ProgressBarSize get() = 50.dp
  val ProgressBarSizeBig get() = 60.dp
  val ErrorLayoutImageSize get() = 90.dp
  val ErrorLayoutTextPadding get() = 32.dp
  val ImageDrawerMargin get() = 16.dp
  val CheckmarkHeight get() = ProgressBarSize
  val CheckmarkWidth get() = (CheckmarkHeight * 1.333f).toInt()
}