package com.arsvechkarev.views.drawables

import android.graphics.Canvas
import android.graphics.Rect
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.TextPaint
import com.arsvechkarev.core.extenstions.getTextHeight
import com.arsvechkarev.core.viewbuilding.Fonts

class LetterInCircleDrawable(
  private val letter: String,
  textColor: Int,
  backgroundColor: Int
) : BaseDrawable() {
  
  private val textPaint = TextPaint(color = textColor, font = Fonts.SegoeUiBold)
  private val paint = Paint(color = backgroundColor)
  private var halfTextHeight = 0f
  
  override fun onBoundsChange(bounds: Rect) {
    computeTextSize()
    halfTextHeight = textPaint.getTextHeight(letter) / 2f
  }
  
  override fun draw(canvas: Canvas) {
    val hw = bounds.width() / 2f
    val hh = bounds.height() / 2f
    canvas.drawCircle(hw, hh, minOf(hw, hh), paint)
    canvas.drawText(letter, hw, hh + halfTextHeight, textPaint)
  }
  
  private fun computeTextSize() {
    textPaint.textSize = 10f
    while (true) {
      val textHeight = textPaint.getTextHeight(letter)
      if (textHeight > bounds.height() * 0.6f) {
        break
      }
      textPaint.textSize++
    }
  }
}