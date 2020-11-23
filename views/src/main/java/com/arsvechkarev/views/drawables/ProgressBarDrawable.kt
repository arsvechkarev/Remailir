package com.arsvechkarev.views.drawables

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.viewbuilding.Colors

class ProgressBarDrawable : BaseDrawable() {
  
  private val innerStartAngle = 50f
  private val outerStartAngle = 120f
  private val sweepAngle = 260f
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    color = Colors.Accent
  }
  private val outerOval = RectF()
  private val innerOval = RectF()
  private var outerRotationAngle = 0f
  private var innerRotationAngle = 0f
  
  fun updateAngles(outer: Float, inner: Float) {
    outerRotationAngle = outer
    innerRotationAngle = inner
  }
  
  override fun onBoundsChange(bounds: Rect) {
    val w = bounds.width().f
    val h = bounds.height().f
    paint.strokeWidth = w / 10f
    val outerInset = paint.strokeWidth / 1.5f
    outerOval.set(outerInset, outerInset, w - outerInset, h - outerInset)
    val innerInset = paint.strokeWidth + outerInset * 2
    innerOval.set(innerInset, innerInset, w - innerInset, h - innerInset)
  }
  
  override fun setAlpha(alpha: Int) {
    paint.alpha = alpha
  }
  
  override fun draw(canvas: Canvas) {
    canvas.execute {
      canvas.translate(bounds.left.f, bounds.top.f)
      val width = bounds.width()
      val height = bounds.height()
      rotate(outerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(outerOval, outerStartAngle, sweepAngle, false, paint)
      rotate(-outerRotationAngle + innerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(innerOval, innerStartAngle, sweepAngle, false, paint)
      invalidateSelf()
    }
  }
}