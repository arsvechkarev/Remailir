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


class WaveDrawerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
    private const val animationDuration = 300L
    private const val constY = 0f
  }
  
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.FILL
  }
  
  private var drawFullCircleAtFirst = false
  private var colorToDraw = -1
  private var colorBackground = -1
  private var defaultRadius = -1f
  
  var radius = Float.MAX_VALUE
    set(value) {
      field = value
      invalidate()
    }
  
  private var circleX = -1f
  private var mode = NORMAL
  
  fun animate(colorToDraw: Int, colorBackground: Int) {
    this.colorToDraw = colorToDraw
    this.colorBackground = colorBackground
    defaultRadius = (height / 2).toFloat()
    radius = defaultRadius
    circleX = width.toFloat()
    paint.color = colorToDraw
    paint.strokeWidth = height.toFloat() * 1.5f
    mode = NORMAL
    invalidate()
  
    val animator = ObjectAnimator.ofFloat(this, "radius", width.toFloat())
    animator.duration = animationDuration
    animator.doOnEnd {
      setBackgroundColor(colorToDraw)
    }
    animator.start()
  }
  
  fun reverse() {
    mode = REVERSE
    val animator = ObjectAnimator.ofFloat(this, "radius", 0f)
    animator.duration = animationDuration
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
    if (radius <= circleX) {
      canvas.drawCircle(circleX, constY, radius, paint)
    }
  }
  
  private fun handleReverseAnimation(canvas: Canvas) {
    if (radius <= circleX) {
      if (drawFullCircleAtFirst) {
        drawFullCircleAtFirst = false
        canvas.drawCircle(circleX, constY, radius, paint)
        setBackgroundColor(colorBackground)
      }
      canvas.drawCircle(circleX, constY, radius, paint)
    }
  }
  
  private enum class Mode {
    NORMAL, REVERSE
  }
}