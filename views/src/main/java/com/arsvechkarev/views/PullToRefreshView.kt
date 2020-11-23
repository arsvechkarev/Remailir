package com.arsvechkarev.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.TextPaint
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.extenstions.getTextHeight
import com.arsvechkarev.core.extenstions.i
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Fonts.SegoeUiBold
import com.arsvechkarev.core.viewbuilding.TextSizes.H4
import com.arsvechkarev.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.viewdsl.DURATION_DEFAULT
import com.arsvechkarev.viewdsl.DURATION_SHORT
import com.arsvechkarev.viewdsl.DURATION_VERY_SHORT
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.cancelIfRunning
import com.arsvechkarev.viewdsl.doOnEnd
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.startIfNotRunning
import com.arsvechkarev.views.drawables.ProgressBarDrawable

class PullToRefreshView(context: Context) : FrameLayout(context) {
  
  private val progressBarSize = 30.dp
  private val innerPadding get() = progressBarSize / 1.8f
  private val cornersRadius = 4.dp.f
  private val progressBarDrawable = ProgressBarDrawable()
  private val arrowDownDrawable = context.retrieveDrawable(R.drawable.ic_arrow_down)
  private var arrowDownDrawableRotation = 0f
  private val textPaint = TextPaint(textSize = H4, font = SegoeUiBold)
  private val backgroundPaint = Paint(Colors.Dialog)
  private val textPullToRefresh = resources.getString(R.string.text_pull_to_refresh)
  private val textReleaseToRefresh = resources.getString(R.string.text_release_to_refresh)
  private val textRefreshing = resources.getString(R.string.text_refreshing)
  private var textPullToRefreshHeight = 0f
  private var textReleaseToRefreshHeight = 0f
  private var textRefreshingHeight = 0f
  private var textPullToRefreshAlpha = 255
  private var textReleaseToRefreshAlpha = 0
  private var textRefreshingAlpha = 0
  private var outerRotationAngle = 0f
  private var innerRotationAngle = 0f
  private var maxTextWidth = 0f
  private var plankWidth = 0f
  private var plankHeight = 0f
  
  internal var isPlankOpened = false
  internal var distanceToTop = 0f
  internal val initialDistanceToTop get() = progressBarSize / 2f
  
  var onRefreshPulled: () -> Unit = {}
  
  private val progressOuterAnimator = ValueAnimator().apply {
    configure(1500L) { outerRotationAngle = animatedValue as Float }
  }
  private val progressInnerAnimator = ValueAnimator().apply {
    configure(800L) { innerRotationAngle = -(animatedValue as Float) }
  }
  private val plankAnimator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      distanceToTop = it.animatedValue as Float
    }
  }
  private val textAndIconAnimator = ValueAnimator().apply {
    duration = DURATION_DEFAULT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      arrowDownDrawable.alpha = ((1 - it.animatedValue as Float) * 255).i
      progressBarDrawable.alpha = (it.animatedValue as Float * 255).i
    }
  }
  private val alphaAnimator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      val alpha = (it.animatedValue as Float * 255).i
      progressBarDrawable.alpha = alpha
      backgroundPaint.alpha = alpha
      textRefreshingAlpha = alpha
    }
  }
  private val arrowDrawableAnimator = ValueAnimator().apply {
    duration = DURATION_VERY_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      arrowDownDrawableRotation = it.animatedValue as Float
      invalidate()
    }
  }
  
  fun hide() {
    if (!isPlankOpened) return
    isPlankOpened = false
    alphaAnimator.setFloatValues(1f, 0f)
    alphaAnimator.doOnEnd {
      distanceToTop = 0f
      arrowDownDrawableRotation = 1f
      arrowDownDrawable.alpha = 255
      progressBarDrawable.alpha = 0
      backgroundPaint.alpha = 255
      textPullToRefreshAlpha = 255
      textRefreshingAlpha = 0
    }
    alphaAnimator.start()
  }
  
  internal fun getPlankHeight(): Float {
    return plankHeight
  }
  
  internal fun animateToRefreshingState() {
    onRefreshPulled()
    plankAnimator.setFloatValues(distanceToTop, initialDistanceToTop * 2 + plankHeight)
    plankAnimator.start()
    textReleaseToRefreshAlpha = 0
    textRefreshingAlpha = 255
    textAndIconAnimator.setFloatValues(0f, 1f)
    textAndIconAnimator.start()
  }
  
  internal fun animateToHiddenState() {
    plankAnimator.setFloatValues(distanceToTop, 0f)
    plankAnimator.doOnEnd { arrowDownDrawable.alpha = 255 }
    plankAnimator.start()
  }
  
  internal fun moveToPullToRefreshState() {
    textPullToRefreshAlpha = 255
    textReleaseToRefreshAlpha = 0
    arrowDrawableAnimator.setFloatValues(arrowDownDrawableRotation, 0f)
    arrowDrawableAnimator.start()
  }
  
  internal fun moveToReleaseToRefreshState() {
    textPullToRefreshAlpha = 0
    textReleaseToRefreshAlpha = 255
    arrowDrawableAnimator.setFloatValues(arrowDownDrawableRotation, 180f)
    arrowDrawableAnimator.start()
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val horizontalPadding = progressBarSize / 2
    val verticalPadding = progressBarSize / 4
    maxTextWidth = maxOf(textPaint.measureText(textPullToRefresh),
      textPaint.measureText(textReleaseToRefresh), textPaint.measureText(textRefreshing))
    plankWidth = progressBarSize + horizontalPadding * 2 +
        innerPadding + maxTextWidth
    textPullToRefreshHeight = textPaint.getTextHeight(textPullToRefresh).toFloat()
    textReleaseToRefreshHeight = textPaint.getTextHeight(textReleaseToRefresh).toFloat()
    textRefreshingHeight = textPaint.getTextHeight(textRefreshing).toFloat()
    val maxTextHeight = maxOf(textPullToRefreshHeight,
      textReleaseToRefreshHeight, textRefreshingHeight).i
    plankHeight = maxOf(progressBarSize, maxTextHeight) + verticalPadding * 2f
    val left = (width / 2f - plankWidth / 2f + horizontalPadding).i
    val top = (plankHeight / 2f - progressBarSize / 2f).i
    progressBarDrawable.setBounds(
      left, top, left + progressBarSize, top + progressBarSize
    )
    arrowDownDrawable.setBounds(
      left, top, left + progressBarSize, top + progressBarSize
    )
    progressBarDrawable.alpha = 0
    arrowDownDrawable.colorFilter = PorterDuffColorFilter(Colors.TextPrimary, SRC_ATOP)
    arrowDownDrawable.alpha = 255
    progressInnerAnimator.startIfNotRunning()
    progressOuterAnimator.startIfNotRunning()
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    val left = width / 2f - plankWidth / 2f
    canvas.execute {
      translate(0f, distanceToTop - initialDistanceToTop - plankHeight)
      canvas.drawRoundRect(
        left, 0f, left + plankWidth, plankHeight,
        cornersRadius, cornersRadius, backgroundPaint
      )
      progressBarDrawable.updateAngles(outerRotationAngle, innerRotationAngle)
      progressBarDrawable.draw(canvas)
      canvas.execute {
        val centerX = arrowDownDrawable.bounds.exactCenterX()
        val centerY = arrowDownDrawable.bounds.exactCenterY()
        rotate(arrowDownDrawableRotation, centerX, centerY)
        arrowDownDrawable.draw(canvas)
      }
      textPaint.alpha = textPullToRefreshAlpha
      canvas.drawText(textPullToRefresh)
      textPaint.alpha = textReleaseToRefreshAlpha
      canvas.drawText(textReleaseToRefresh)
      textPaint.alpha = textRefreshingAlpha
      canvas.drawText(textRefreshing)
    }
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    plankAnimator.cancelIfRunning()
    alphaAnimator.cancelIfRunning()
    progressInnerAnimator.cancelIfRunning()
    progressOuterAnimator.cancelIfRunning()
    arrowDrawableAnimator.cancelIfRunning()
  }
  
  private fun Canvas.drawText(text: String) {
    val x = maxTextWidth / 2 + innerPadding + progressBarDrawable.bounds.right
    val adjustedHeight = textPaint.getTextHeight(text)
    val y = plankHeight / 2f + adjustedHeight / 2
    drawText(text, x, y, textPaint)
  }
  
  private fun ValueAnimator.configure(
    duration: Long,
    onUpdate: ValueAnimator.() -> Unit
  ) {
    setFloatValues(0f, 360f)
    addUpdateListener {
      onUpdate(this)
      invalidate()
    }
    interpolator = LinearInterpolator()
    repeatCount = ValueAnimator.INFINITE
    this.duration = duration
  }
}