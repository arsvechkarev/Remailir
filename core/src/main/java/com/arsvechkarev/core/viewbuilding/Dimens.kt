package com.arsvechkarev.core.viewbuilding

import com.arsvechkarev.viewdsl.Ints.dp

object Dimens {
  
  val IconLogoSize get() = 100.dp
  val DefaultCornerRadius get() = 6.dp
  val UserIconSize get() = 40.dp
  val DividerHeight get() = 1.dp
  val ChipHorizontalPadding get() = 20.dp
  val ChipVerticalPadding get() = 4.dp
  val ChipStrokeSize get() = 2.dp
  val DividerMargin get() = 12.dp
  val ToolbarMargin get() = 16.dp
  val ToolbarImageSize get() = 24.dp
  val ProgressBarSizeSmall get() = 30.dp
  val ProgressBarSize get() = 50.dp
  val ProgressBarSizeBig get() = 60.dp
  val ErrorLayoutImageSize get() = 90.dp
  val ErrorLayoutTextPadding get() = 32.dp
  val CheckmarkHeight get() = ProgressBarSize
  val CheckmarkWidth get() = (CheckmarkHeight * 1.333f).toInt()
  val CheckmarkHeightSmall get() = 18.dp
  val CheckmarkWidthSmall get() = (CheckmarkHeightSmall * 1.333f).toInt()
}