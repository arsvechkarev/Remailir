package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import com.arsvechkarev.views.WaveDrawerView.Mode.NORMAL
import com.arsvechkarev.views.WaveDrawerView.Mode.REVERSE


class WaveDrawerView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
  
  companion object {
  
    private const val dx = 150f
    private const val DELAY_INVALIDATE_ON_DRAW = 10L
  }
  
  private var path = Path()
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
  }
  private var startTime = -1L
  private var colorToDraw = -1
  private var colorBackground = -1
  private var currentX = 0f
  private var yPosition = 0f
  private var isAnimating = false
  private var mode = NORMAL
  
  fun animate(colorToDraw: Int, colorBackground: Int) {
    this.colorToDraw = colorToDraw
    this.colorBackground = colorBackground
    yPosition = (height / 2).toFloat()
    currentX = width.toFloat()
    paint.color = colorToDraw
    paint.strokeWidth = height.toFloat() * 1.5f
    startTime = SystemClock.elapsedRealtime()
    mode = NORMAL
    isAnimating = true
    invalidate()
  }
  
  fun reverse() {
    paint.color = colorBackground
    mode = REVERSE
    isAnimating = true
    invalidate()
  }
  
  override fun onDraw(canvas: Canvas) {
    if (!isAnimating) return
    when (mode) {
      NORMAL -> handleNormalAnimation(canvas)
      REVERSE -> handleReverseAnimation(canvas)
    }
  }
  
  private fun handleNormalAnimation(canvas: Canvas) {
    if (currentX >= 0) {
      path.moveTo(currentX, yPosition)
      path.lineTo(currentX, yPosition)
      canvas.drawPath(path, paint)
      currentX -= dx
      if (currentX <= 0) {
        setBackgroundColor(colorToDraw)
      }
      postInvalidateDelayed(DELAY_INVALIDATE_ON_DRAW)
    } else {
      path.reset()
    }
  }
  
  private fun handleReverseAnimation(canvas: Canvas) {
    if (currentX <= width) {
      path.moveTo(currentX, yPosition)
      path.lineTo(currentX, yPosition)
      canvas.drawPath(path, paint)
      currentX += dx
      if (currentX >= width) {
        setBackgroundColor(colorBackground)
      }
      postInvalidateDelayed(DELAY_INVALIDATE_ON_DRAW)
    } else {
      path.reset()
      path.moveTo(currentX, yPosition)
    }
  }
  
  private enum class Mode {
    NORMAL, REVERSE
  }
}