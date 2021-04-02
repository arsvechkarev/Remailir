package com.arsvechkarev.views.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.BoringLayout
import android.text.Layout
import android.text.TextPaint
import android.text.TextUtils
import android.widget.TextView
import core.resources.Colors
import core.resources.Fonts

val TEMP_RECT = Rect()
val TEMP_RECT_F = RectF()
val TEMP_PAINT = TextPaint(Paint.ANTI_ALIAS_FLAG)

fun Paint(
  color: Int
) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
  this.color = color
}

fun TextPaint(
  textSize: Float = 0f,
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

fun TextPaint.getTextHeight(text: String = "Agy"): Int {
  TEMP_RECT.setEmpty()
  getTextBounds(text, 0, text.length, TEMP_RECT)
  return TEMP_RECT.height()
}

inline fun Canvas.execute(action: Canvas.() -> Unit) {
  val count = save()
  action(this)
  restoreToCount(count)
}

fun boringLayoutOf(
  textPaint: TextPaint,
  text: CharSequence,
  maxWidth: Float = -1f,
  alignment: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
): BoringLayout {
  val metrics = BoringLayout.isBoring(text, textPaint)
  return if (maxWidth == -1f) {
    BoringLayout.make(text, textPaint, metrics.width,
      alignment, 0f, 0f, metrics, false)
  } else {
    BoringLayout.make(text, textPaint, metrics.width,
      alignment, 0f, 0f, metrics, false,
      TextUtils.TruncateAt.END, maxWidth.toInt())
  }
}