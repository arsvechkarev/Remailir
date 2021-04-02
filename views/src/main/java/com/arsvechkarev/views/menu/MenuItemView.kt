package com.arsvechkarev.views.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import com.arsvechkarev.views.utils.Paint
import com.arsvechkarev.views.utils.TextPaint
import com.arsvechkarev.views.utils.getTextHeight
import viewdsl.Ints.dp
import viewdsl.retrieveDrawable
import viewdsl.rippleBackground

class MenuItemView(
  context: Context,
  iconRes: Int,
  textSize: Float,
  private val circleSize: Int,
  private val text: String,
) : View(context) {
  
  private val icon = context.retrieveDrawable(iconRes)
  private val textPaint = TextPaint(textSize, font = core.resources.Fonts.SegoeUiBold)
  private val circlePaint = Paint(core.resources.Colors.OnAccent)
  
  private var firstWord: String? = null
  private var secondWord: String? = null
  
  init {
    rippleBackground(
      core.resources.Colors.Ripple, core.resources.Colors.Transparent,
      core.resources.Dimens.DefaultCornerRadius)
    val padding = 6.dp
    setPadding(padding, padding, padding, padding)
    if (text.contains(' ')) {
      firstWord = text.substringBefore(' ')
      secondWord = text.substringAfter(' ')
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val textWidth: Int
    val textHeight: Int
    if (firstWord != null && secondWord != null) {
      val firstWidth = textPaint.measureText(firstWord!!)
      val secondWidth = textPaint.measureText(secondWord!!)
      textWidth = maxOf(firstWidth, secondWidth).toInt()
      textHeight = textPaint.getTextHeight(firstWord!!) +
          textPaint.getTextHeight(secondWord!!) + (getTextPadding() * 2.5f).toInt()
    } else {
      textWidth = textPaint.measureText(text).toInt()
      textHeight = textPaint.getTextHeight() + (getTextPadding() * 1.5f).toInt()
    }
    val width = maxOf(circleSize, textWidth) + paddingStart + paddingEnd
    val height = circleSize + textHeight + paddingTop + paddingBottom
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec),
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    icon.colorFilter = PorterDuffColorFilter(core.resources.Colors.Icon, PorterDuff.Mode.SRC_ATOP)
    val padding = circleSize / 4
    icon.setBounds(
      w / 2 - circleSize / 2 + padding,
      padding,
      w / 2 + circleSize / 2 - padding,
      circleSize - padding
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, circleSize / 2f, circleSize / 2f - paddingTop, circlePaint)
    icon.draw(canvas)
    if (firstWord != null && secondWord != null) {
      var y = circleSize + getTextPadding().toFloat() + textPaint.getTextHeight(firstWord!!)
      canvas.drawText(firstWord!!, width / 2f, y, textPaint)
      y += textPaint.getTextHeight(firstWord!!) + getTextPadding()
      canvas.drawText(secondWord!!, width / 2f, y, textPaint)
    } else {
      val y = circleSize + getTextPadding().toFloat() + textPaint.getTextHeight()
      canvas.drawText(text, width / 2f, y, textPaint)
    }
  }
  
  private fun getTextPadding(): Int {
    if (firstWord != null && secondWord != null) {
      return (textPaint.getTextHeight() * 0.8f).toInt()
    }
    return (textPaint.getTextHeight())
  }
}