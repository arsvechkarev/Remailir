package com.arsvechkarev.core.extenstions

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import android.widget.TextView
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Fonts

val TEMP_RECT = Rect()
val TEMP_RECT_F = RectF()
val TEMP_PAINT = TextPaint(Paint.ANTI_ALIAS_FLAG)

fun Any.Paint(
  color: Int
) = android.graphics.Paint(Paint.ANTI_ALIAS_FLAG).apply {
  this.color = color
}

fun Any.TextPaint(
  textSize: Float,
  color: Int = Colors.TextPrimary,
  textAlign: Paint.Align = Paint.Align.CENTER,
  font: Typeface = Fonts.SegoeUi
) = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
  this.textSize = textSize
  this.color = color
  this.textAlign = textAlign
  this.typeface = font
}

fun TextView.textHeight(text: String): Int {
  TEMP_PAINT.typeface = typeface
  TEMP_PAINT.textSize = textSize
  TEMP_PAINT.getTextBounds(text, 0, text.length, TEMP_RECT)
  return TEMP_RECT.height()
}

fun TextPaint.getTextHeight(text: String): Int {
  TEMP_RECT.setEmpty()
  getTextBounds(text, 0, text.length, TEMP_RECT)
  return TEMP_RECT.height()
}

inline fun Canvas.execute(action: Canvas.() -> Unit) {
  val count = save()
  action(this)
  restoreToCount(count)
}