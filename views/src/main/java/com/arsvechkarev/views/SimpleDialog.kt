package com.arsvechkarev.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.graphics.ColorUtils
import config.DurationsConfigurator
import core.resources.Colors
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.cancelIfRunning
import viewdsl.contains
import viewdsl.gone
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.visible
import kotlin.math.abs
import kotlin.math.hypot

class SimpleDialog(context: Context) : FrameLayout(context) {
  
  private var latestX = 0f
  private var latestY = 0f
  private var currentShadowFraction = 0f
  private val dialogView get() = getChildAt(0)
  private val shadowAnimator = ValueAnimator().apply {
    interpolator = AccelerateDecelerateInterpolator
    duration = DurationsConfigurator.DurationShort
    addUpdateListener {
      currentShadowFraction = it.animatedValue as Float
      onShadowFractionChangedListener?.invoke(currentShadowFraction)
      setBackgroundColor(getShadowColor(currentShadowFraction))
    }
  }
  
  var onShadowFractionChangedListener: ((Float) -> Unit)? = null
  var onHide: () -> Unit = {}
  
  var isOpened = false
    private set
  
  init {
    invisible()
  }
  
  override fun onViewAdded(child: View) {
    if (child === dialogView) {
      dialogView.layoutGravity(Gravity.CENTER)
    }
  }
  
  fun show(animate: Boolean) {
    if (isOpened) return
    isOpened = true
    visible()
    dialogView.visible()
    if (animate) {
      dialogView.alpha = 0f
      shadowAnimator.cancelIfRunning()
      shadowAnimator.setFloatValues(currentShadowFraction, 1f)
      shadowAnimator.start()
      dialogView.animate()
          .withLayer()
          .scaleX(1f)
          .scaleY(1f)
          .alpha(1f)
          .setDuration(DurationsConfigurator.DurationShort)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .start()
    } else {
      setBackgroundColor(Colors.Shadow)
      currentShadowFraction = 1f
      setBackgroundColor(getShadowColor(currentShadowFraction))
      dialogView.scaleX = 1f
      dialogView.scaleY = 1f
      dialogView.alpha = 1f
    }
  }
  
  fun hide(animate: Boolean) {
    if (!isOpened) return
    isOpened = false
    if (animate) {
      shadowAnimator.cancelIfRunning()
      shadowAnimator.setFloatValues(currentShadowFraction, 0f)
      shadowAnimator.start()
      dialogView.animate()
          .withLayer()
          .alpha(0f)
          .scaleX(SCALE_FACTOR)
          .scaleY(SCALE_FACTOR)
          .setDuration(DurationsConfigurator.DurationShort)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .withEndAction {
            onHide()
            gone()
          }
          .start()
    } else {
      currentShadowFraction = SCALE_FACTOR
      setBackgroundColor(getShadowColor(currentShadowFraction))
      dialogView.invisible()
      gone()
      onHide()
    }
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        if (event !in dialogView) {
          latestX = event.x
          latestY = event.y
          return true
        }
      }
      ACTION_UP -> {
        val dx = abs(event.x - latestX)
        val dy = abs(event.y - latestY)
        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        if (hypot(dx, dy) < scaledTouchSlop) {
          hide(animate = true)
        }
      }
    }
    return super.onTouchEvent(event)
  }
  
  private fun getShadowColor(fraction: Float): Int {
    return ColorUtils.blendARGB(Color.TRANSPARENT, Colors.Shadow, fraction)
  }
  
  companion object {
    
    private const val SCALE_FACTOR = 0.8f
  }
}