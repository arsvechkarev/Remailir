package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.TextPaint
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.extenstions.getTextHeight
import com.arsvechkarev.core.extenstions.i
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.isOrientationPortrait
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.rippleBackground

class TextWithImageView(
  context: Context,
  iconRes: Int,
  textSize: Float,
  circleColor: Int,
  private val text: String
) : View(context) {
  
  private val icon = context.retrieveDrawable(iconRes)
  private val textPaint = TextPaint(textSize, font = Fonts.SegoeUiBold)
  private val circlePaint = Paint(circleColor)
  
  private val circlePortraitSize = 70.dp
  private val circleLandscapeSize = 100.dp
  
  init {
    rippleBackground(Colors.Ripple, Colors.Transparent, Dimens.DefaultCornerRadius)
    val padding = 6.dp
    setPadding(padding, padding, padding, padding)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val circleSize = if (isOrientationPortrait) circlePortraitSize else circleLandscapeSize
    val textWidth = textPaint.measureText(text).i
    val textHeight = textPaint.getTextHeight() + (getTextPadding() * 1.5f).toInt()
    val width = maxOf(circleSize, textWidth) + paddingStart + paddingEnd
    val height = circleSize + textHeight + paddingTop + paddingBottom
    setMeasuredDimension(width, height)
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val circleSize = if (isOrientationPortrait) circlePortraitSize else circleLandscapeSize
    icon.colorFilter = PorterDuffColorFilter(Colors.Icon, PorterDuff.Mode.SRC_ATOP)
    val padding = circleSize / 4
    icon.setBounds(
      w / 2 - circleSize / 2 + padding,
      padding,
      w / 2 + circleSize / 2 - padding,
      circleSize - padding
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    val circleSize = if (isOrientationPortrait) circlePortraitSize else circleLandscapeSize
    canvas.drawCircle(width / 2f, circleSize / 2f, circleSize / 2f - paddingTop, circlePaint)
    icon.draw(canvas)
    val y = circleSize + getTextPadding().f + textPaint.getTextHeight()
    canvas.drawText(text, width / 2f, y, textPaint)
  }
  
  private fun getTextPadding(): Int = textPaint.getTextHeight()
}