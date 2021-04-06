package core.resources

import viewdsl.Ints.dp

object Dimens {
  
  val MarginVerySmall get() = 4.dp
  val MarginSmall get() = 8.dp
  val MarginDefault get() = 16.dp
  val MarginBig get() = 24.dp
  
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
  val FailureLayoutImageSize get() = 90.dp
  val FailureLayoutTextPadding get() = 32.dp
  val CheckmarkHeight get() = ProgressBarSize
  val CheckmarkWidth get() = (CheckmarkHeight * 1.333f).toInt()
  val CheckmarkHeightSmall get() = 18.dp
  val CheckmarkWidthSmall get() = (CheckmarkHeightSmall * 1.333f).toInt()
  val MessageStickingDistance get() = 10.dp
}