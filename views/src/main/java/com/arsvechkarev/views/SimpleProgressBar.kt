package com.arsvechkarev.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.arsvechkarev.views.utils.execute
import viewdsl.Ints.dp
import viewdsl.cancelIfRunning

class SimpleProgressBar(context: Context) : View(context) {
  
  private val innerStartAngle = 50f
  private val outerStartAngle = 120f
  private val minSize = 32.dp
  private val sweepAngle = 260f
  
  private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeCap = Paint.Cap.ROUND
    color = core.resources.Colors.Accent
  }
  private val outerOval = RectF()
  private val innerOval = RectF()
  private var outerRotationAngle = 0f
  private var innerRotationAngle = 0f
  
  private val outerAnimator = ValueAnimator().apply {
    configure(1500L) { outerRotationAngle = animatedValue as Float }
  }
  private val innerAnimator = ValueAnimator().apply {
    configure(800L) { innerRotationAngle = -(animatedValue as Float) }
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    innerAnimator.start()
    outerAnimator.start()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = resolveSize(minSize, widthMeasureSpec)
    val height = resolveSize(minSize, heightMeasureSpec)
    setMeasuredDimension(width, height)
  }
  
  override fun onSizeChanged(width: Int, height: Int, oldw: Int, oldh: Int) {
    val w = width.toFloat()
    val h = height.toFloat()
    paint.strokeWidth = w / 10f
    val outerInset = paint.strokeWidth / 1.5f
    outerOval.set(outerInset, outerInset, w - outerInset, h - outerInset)
    val innerInset = paint.strokeWidth + outerInset * 2
    innerOval.set(innerInset, innerInset, w - innerInset, h - innerInset)
  }
  
  override fun onDraw(canvas: Canvas) {
    canvas.execute {
      rotate(outerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(outerOval, outerStartAngle, sweepAngle, false, paint)
      rotate(-outerRotationAngle + innerRotationAngle, width / 2f, height / 2f)
      canvas.drawArc(innerOval, innerStartAngle, sweepAngle, false, paint)
    }
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    innerAnimator.cancelIfRunning()
    outerAnimator.cancelIfRunning()
  }
  
  private fun ValueAnimator.configure(duration: Long, onUpdate: ValueAnimator.() -> Unit) {
    setFloatValues(0f, 360f)
    addUpdateListener {
      onUpdate(this)
      invalidate()
    }
    repeatCount = ValueAnimator.INFINITE
    this.duration = duration
  }
}