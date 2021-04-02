package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import com.arsvechkarev.views.utils.Paint
import com.arsvechkarev.views.utils.TextPaint
import com.arsvechkarev.views.utils.getTextHeight
import core.resources.Colors
import core.resources.Dimens
import core.resources.Fonts
import viewdsl.Ints.dp
import viewdsl.isOrientationPortrait
import viewdsl.retrieveDrawable
import viewdsl.rippleBackground

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
    val circleSize = if (context.isOrientationPortrait) circlePortraitSize else circleLandscapeSize
    val textWidth = textPaint.measureText(text).toInt()
    val textHeight = textPaint.getTextHeight() + (getTextPadding() * 1.5f).toInt()
    val width = maxOf(circleSize, textWidth) + paddingStart + paddingEnd
    val height = circleSize + textHeight + paddingTop + paddingBottom
    setMeasuredDimension(width, height)
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val circleSize = if (context.isOrientationPortrait) circlePortraitSize else circleLandscapeSize
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
    val circleSize = if (context.isOrientationPortrait) circlePortraitSize else circleLandscapeSize
    canvas.drawCircle(width / 2f, circleSize / 2f, circleSize / 2f - paddingTop, circlePaint)
    icon.draw(canvas)
    val y = circleSize + getTextPadding().toFloat() + textPaint.getTextHeight()
    canvas.drawText(text, width / 2f, y, textPaint)
  }
  
  private fun getTextPadding(): Int = textPaint.getTextHeight()
}