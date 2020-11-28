package com.arsvechkarev.common

import android.view.Gravity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens.ErrorLayoutImageSize
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.behavior
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.marginHorizontal
import com.arsvechkarev.viewdsl.paddingVertical
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.withViewBuilder
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior

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
    ImageView(ErrorLayoutImageSize, ErrorLayoutImageSize) {
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