package core.resources

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.TextView
import viewdsl.Ints.dp
import viewdsl.background
import viewdsl.font
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.rippleBackground
import viewdsl.screenWidth
import viewdsl.textColor
import viewdsl.textSize

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
  
  val MessageTextView: TextView.() -> Unit = {
    textColor(Colors.TextPrimary)
    textSize(TextSizes.H4)
    maxWidth = (context.screenWidth * 0.7f).toInt()
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
  
  val Snackbar: View.() -> Unit = {
    margin(Dimens.MarginDefault)
    layoutGravity(Gravity.BOTTOM)
  }
}