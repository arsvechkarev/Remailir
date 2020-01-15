package com.arsvechkarev.views

import android.annotation.SuppressLint
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
    
    private const val frameAmount = 8
    private const val DELAY_INVALIDATE_ON_DRAW = 8L
  }
  
  private var path = Path()
  private val paths = ArrayList<Path>()
  private val paint = Paint(ANTI_ALIAS_FLAG).apply {
    style = Paint.Style.STROKE
    strokeJoin = Paint.Join.ROUND
    strokeCap = Paint.Cap.ROUND
  }
  private var startTime = -1L
  private var dx = 0f
  private var currentX = 0f
  private var yPosition = 0f
  private var isAnimating = false
  private var mode = NORMAL
  
  fun animate(color: Int) {
    this.dx = (width / frameAmount).toFloat()
    yPosition = (height / 2).toFloat()
    currentX = width.toFloat()
    paint.color = color
    paint.strokeWidth = height.toFloat() * 1.5f
    startTime = SystemClock.elapsedRealtime()
    invalidate()
    mode = NORMAL
    isAnimating = true
  }
  
  fun reverse() {
    mode = REVERSE
    isAnimating = true
    invalidate()
  }
  
  @SuppressLint("DrawAllocation")
  override fun onDraw(canvas: Canvas) {
    if (!isAnimating) return
    paths.forEach { canvas.drawPath(it, paint) }
    when (mode) {
      NORMAL -> handleNormalAnimation()
      REVERSE -> handleReverseAnimation()
    }
  }
  
  private fun handleNormalAnimation() {
    if (currentX >= 0) {
      path = Path()
      paths.add(path)
      path.moveTo(currentX, yPosition)
      path.lineTo(currentX, yPosition)
      currentX -= dx
      postInvalidateDelayed(DELAY_INVALIDATE_ON_DRAW)
    }
  }
  
  private fun handleReverseAnimation() {
    if (currentX < width) {
      if (paths.isNotEmpty()) {
        paths.remove(paths.last())
        postInvalidateDelayed(DELAY_INVALIDATE_ON_DRAW)
      }
    } else {
      path.reset()
      path.moveTo(currentX, yPosition)
      currentX += dx
    }
  }
  
  
  private enum class Mode {
    NORMAL, REVERSE
  }
}