package com.arsvechkarev.common

import android.view.Gravity
import android.widget.LinearLayout
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens.UserIconSize
import com.arsvechkarev.core.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.Size.IntSize
import com.arsvechkarev.viewdsl.ViewBuilder
import com.arsvechkarev.viewdsl.background
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.rippleBackground
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.drawables.ProfileDrawable

fun ViewBuilder.UserItemView(): LinearLayout {
  return RootHorizontalLayout(MatchParent, WrapContent) {
    paddings(start = 20.dp, top = 12.dp, end = 16.dp, bottom = 12.dp)
    rippleBackground(Colors.Ripple)
    gravity(Gravity.CENTER_VERTICAL)
    View(IntSize(UserIconSize), IntSize(UserIconSize)) {
      background(ProfileDrawable(context, inverseColors = true))
      margins(end = 24.dp)
    }
    TextView(WrapContent, WrapContent, style = BaseTextView) {
      classNameTag()
      textColor(Colors.TextPrimary)
      textSize(TextSizes.H3)
    }
  }
}