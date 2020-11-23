package com.arsvechkarev.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.cancelIfRunning

class ComplexProgressBar(context: Context) : View(context) {
  
  private val innerStartAngle = 50f
  private val middleStartAngle = 75f
  private val outerStartAngle = 120f
  private val minSize = 32.dp
  private val sweepAngle = 260f
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    color = Colors.Accent
  }
  private val outerOval = RectF()
  private val middleOval = RectF()
  private val innerOval = RectF()
  private var outerRotationAngle = 0f
  private var middleRotationAngle = 0f
  private var innerRotationAngle = 0f
  
  private val outerAnimator = ValueAnimator().apply {
    configure(1500L) { outerRotationAngle = animatedValue as Float }
  }
  
  private val middleAnimator = ValueAnimator().apply {
    configure(2000L) { middleRotationAngle = -(animatedValue as Float) }
  }
  
  private val innerAnimator = ValueAnimator().apply {
    configure(800L) { innerRotationAngle = animatedValue as Float }
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    innerAnimator.start()
    middleAnimator.start()
    outerAnimator.start()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = resolveSize(minSize, widthMeasureSpec)
    val height = resolveSize(minSize, heightMeasureSpec)
    setMeasuredDimension(width, height)
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    paint.strokeWidth = w / 12f
    val outerInset = paint.strokeWidth / 2
    outerOval.set(paddingStart.f + outerInset, paddingTop.f + outerInset,
      w.f - paddingEnd - outerInset, h.f - paddingBottom - outerInset)
    val middleOffset = paint.strokeWidth * 1.5f + outerInset
    middleOval.set(paddingStart.f + middleOffset, paddingTop + middleOffset,
      w.f - paddingEnd - middleOffset, h.f - paddingBottom - middleOffset)
    val innerInset = paint.strokeWidth * 3 + outerInset
    innerOval.set(paddingStart.f + innerInset, paddingTop + innerInset,
      w.f - paddingEnd - innerInset, h.f - paddingBottom - innerInset)
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      rotate(outerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(outerOval, outerStartAngle, sweepAngle, false, paint)
      rotate(-outerRotationAngle + middleRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(middleOval, middleStartAngle, sweepAngle, false, paint)
      rotate(-middleRotationAngle + innerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(innerOval, innerStartAngle, sweepAngle, false, paint)
    }
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    innerAnimator.cancelIfRunning()
    middleAnimator.cancelIfRunning()
    outerAnimator.cancelIfRunning()
  }
  
  private fun ValueAnimator.configure(duration: Long, onUpdate: ValueAnimator.() -> Unit) {
    setFloatValues(0f, 360f)
    addUpdateListener {
      onUpdate(this)
      invalidate()
    }
    interpolator = AccelerateDecelerateInterpolator
    repeatCount = ValueAnimator.INFINITE
    this.duration = duration
  }
}