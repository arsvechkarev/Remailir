package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.text.Layout
import android.text.TextPaint
import android.view.View
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Dimens.ChipHorizontalPadding
import com.arsvechkarev.core.viewbuilding.Dimens.ChipVerticalPadding
import com.arsvechkarev.core.viewbuilding.Fonts

class Chip(
  context: Context,
  text: String,
  textSize: Float,
  private val colorPrimary: Int,
  private val colorSecondary: Int,
) : View(context) {
  
  private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).also {
    it.typeface = Fonts.SegoeUiBold
    it.textSize = textSize
    it.color = colorPrimary
  }
  private val rectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeWidth = Dimens.ChipStrokeSize.toFloat()
    color = colorPrimary
  }
  
  private val rect = RectF()
  private val textLayout: Layout
  
  val text get() = textLayout.text.toString()
  
  init {
    textLayout = boringLayoutOf(textPaint, text)
    setPadding(ChipHorizontalPadding, ChipVerticalPadding, ChipHorizontalPadding,
      ChipVerticalPadding)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = paddingStart + textLayout.width + paddingEnd + rectPaint.strokeWidth * 2
    val height = textLayout.height + paddingTop + paddingBottom + rectPaint.strokeWidth * 2
    setMeasuredDimension(
      resolveSize(width.toInt(), widthMeasureSpec),
      resolveSize(height.toInt(), heightMeasureSpec)
    )
  }
  
  override fun onDraw(canvas: Canvas) {
    val strokeOffsetForRect: Float
    if (isSelected) {
      strokeOffsetForRect = rectPaint.strokeWidth / 2f
      rectPaint.style = Paint.Style.FILL
      textPaint.color = colorSecondary
    } else {
      strokeOffsetForRect = rectPaint.strokeWidth
      rectPaint.style = Paint.Style.STROKE
      textPaint.color = colorPrimary
    }
    rect.set(strokeOffsetForRect, strokeOffsetForRect,
      width - strokeOffsetForRect, height - strokeOffsetForRect)
    canvas.drawRoundRect(rect, height.f, height.f, rectPaint)
    val strokeOffsetItems = rectPaint.strokeWidth
    canvas.execute {
      translate(paddingStart.f + strokeOffsetItems,
        paddingTop.f + strokeOffsetItems)
      textLayout.draw(canvas)
    }
  }
}