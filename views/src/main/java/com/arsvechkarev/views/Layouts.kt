package com.arsvechkarev.views

import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import core.resources.Colors
import core.resources.Dimens.FailureLayoutImageSize
import core.resources.Styles
import core.resources.TextSizes
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.behavior
import viewdsl.gravity
import viewdsl.image
import viewdsl.invisible
import viewdsl.marginHorizontal
import viewdsl.paddingVertical
import viewdsl.tag
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

const val LayoutError = "LayoutError"
const val ImageError = "ImageError"
const val TextError = "TextError"
const val TextRetry = "TextRetry"

fun CoordinatorLayout.ErrorLayout() = withViewBuilder {
  VerticalLayout(MatchParent, MatchParent) {
    tag(LayoutError)
    invisible()
    gravity(Gravity.CENTER)
    behavior(ViewUnderHeaderBehavior())
    ImageView(FailureLayoutImageSize, FailureLayoutImageSize) {
      tag(ImageError)
      image(R.drawable.image_unknown_error)
    }
    TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
      tag(TextError)
      textSize(TextSizes.H3)
      gravity(Gravity.CENTER)
      paddingVertical(24.dp)
      marginHorizontal(32.dp)
    }
    TextView(WrapContent, WrapContent, style = Styles.ClickableButton(
      Colors.ErrorGradientStart, Colors.ErrorGradientEnd
    )) {
      tag(TextRetry)
      textSize(TextSizes.H3)
      text(R.string.text_retry)
    }
  }
}