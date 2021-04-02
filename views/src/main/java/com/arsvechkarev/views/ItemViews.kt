package com.arsvechkarev.views

import android.view.Gravity
import android.widget.LinearLayout
import com.arsvechkarev.views.drawables.ProfileDrawable
import core.resources.Colors
import core.resources.Dimens.UserIconSize
import core.resources.Styles.BaseTextView
import core.resources.TextSizes
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.ViewBuilder
import viewdsl.background
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.margins
import viewdsl.paddings
import viewdsl.rippleBackground
import viewdsl.textColor
import viewdsl.textSize

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