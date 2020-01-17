package com.arsvechkarev.views

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import com.arsvechkarev.views.WaveDrawerView.Mode.NORMAL
import com.arsvechkarev.views.WaveDrawerView.Mode.REVERSE


/**
 * Shows wave effect from right to bottom and vice versa with specified colors
 */
class WaveDrawerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private const val DURATION_DEFAULT = 300L
    private const val DURATION_SHORT = 50L
  }
  
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }
  private var circleY = -1f
  
  private var drawRightAway = false
  private var drawFullCircleAtFirst = false
  private var colorToDraw = -1
  private var colorBackground = -1
  private var defaultRadius = -1f
  private var circleX = -1f
  
  private var mode = NORMAL
  private var radius = Float.MAX_VALUE
    set(value) {
      field = value
      invalidate()
    }
  
  fun animate(
    colorToDraw: Int,
    colorBackground: Int,
    quickly: Boolean = false,
    onEnd: () -> Unit
  ) {
    this.colorToDraw = colorToDraw
    this.colorBackground = colorBackground
    val halfHeight = (height / 2).toFloat()
    circleY = halfHeight
    defaultRadius = halfHeight
    radius = defaultRadius
    circleX = width.toFloat()
    paint.color = colorToDraw
    mode = NORMAL
    invalidate()
    
    val animator = ObjectAnimator.ofFloat(this, "radius", width.toFloat())
    animator.duration = if (quickly) DURATION_SHORT else DURATION_DEFAULT
    animator.doOnEnd {
      setBackgroundColor(colorToDraw)
      onEnd()
    }
    animator.start()
  }
  
  fun reverse() {
    mode = REVERSE
    val animator = ObjectAnimator.ofFloat(this, "radius", width.toFloat(), 0f)
    animator.duration = DURATION_DEFAULT
    drawFullCircleAtFirst = true
    animator.start()
  }
  
  override fun onDraw(canvas: Canvas) {
    when (mode) {
      NORMAL -> handleNormalAnimation(canvas)
      REVERSE -> handleReverseAnimation(canvas)
    }
  }
  
  private fun handleNormalAnimation(canvas: Canvas) {
    if (drawRightAway) {
      canvas.drawCircle(0f, 0f, width.toFloat() + width / 6, paint)
      drawRightAway = false
    }
    if (radius <= circleX) {
      canvas.drawCircle(circleX, circleY, radius, paint)
    }
  }
  
  private fun handleReverseAnimation(canvas: Canvas) {
    if (radius <= circleX) {
      if (drawFullCircleAtFirst) {
        drawFullCircleAtFirst = false
        canvas.drawCircle(circleX, circleY, radius, paint)
        setBackgroundColor(colorBackground)
      }
      canvas.drawCircle(circleX, circleY, radius, paint)
    }
  }
  
  private enum class Mode {
    NORMAL, REVERSE
  }
}