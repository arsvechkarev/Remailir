package com.arsvechkarev.core.viewbuilding

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.TextUtils
import android.widget.TextView
import com.arsvechkarev.core.R
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.background
import com.arsvechkarev.viewdsl.drawables
import com.arsvechkarev.viewdsl.font
import com.arsvechkarev.viewdsl.paddingHorizontal
import com.arsvechkarev.viewdsl.paddingVertical
import com.arsvechkarev.viewdsl.rippleBackground
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize

@Suppress("FunctionName")
object Styles {
  
  val BaseTextView: TextView.() -> Unit = {
    textSize(TextSizes.H5)
    font(Fonts.SegoeUi)
    textColor(Colors.TextPrimary)
    ellipsize = TextUtils.TruncateAt.END
  }
  
  val BoldTextView: TextView.() -> Unit = {
    font(Fonts.SegoeUiBold)
    textColor(Colors.TextPrimary)
    textSize(TextSizes.H4)
  }
  
  fun ClickableTextView(
    rippleColor: Int,
    backgroundColor: Int,
  ): TextView.() -> Unit = {
    textColor(Colors.TextPrimary)
    textSize(TextSizes.H4)
    font(Typeface.DEFAULT_BOLD)
    paddingVertical(6.dp)
    paddingHorizontal(12.dp)
    rippleBackground(rippleColor, backgroundColor, 4.dp)
  }
  
  fun ClickableButton(
    colorStart: Int = Colors.Accent,
    colorEnd: Int = Colors.AccentLight,
  ): TextView.() -> Unit = {
    apply(BoldTextView)
    val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.BL_TR, intArrayOf(
      colorStart, colorEnd))
    val r = 120.dp.toFloat()
    val outerRadii = floatArrayOf(r, r, r, r, r, r, r, r)
    gradientDrawable.cornerRadii = outerRadii
    val roundRectShape = RoundRectShape(outerRadii, null, null)
    val maskRect = ShapeDrawable().apply {
      shape = roundRectShape
      paint.color = Colors.Ripple
    }
    val colorStateList = ColorStateList.valueOf(Colors.Ripple)
    background(RippleDrawable(colorStateList, gradientDrawable, maskRect))
    paddingVertical(6.dp)
    paddingHorizontal(20.dp)
    textSize(TextSizes.H3)
    isClickable = true
    isFocusable = true
  }
}