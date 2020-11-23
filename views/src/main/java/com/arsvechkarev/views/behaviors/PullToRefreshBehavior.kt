package com.arsvechkarev.views.behaviors

import android.content.Context
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arsvechkarev.views.PullToRefreshView
import kotlin.math.sqrt

class PullToRefreshBehavior(
  private val context: Context
) : CoordinatorLayout.Behavior<PullToRefreshView>() {
  
  private var isPlankOpened = false
  private var isBeingDragged = false
  private var latestDownY = 0f
  
  var allowPulling: () -> Boolean = { false }
  
  override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: PullToRefreshView, ev: MotionEvent): Boolean {
    if (isPlankOpened || !allowPulling()) return false
    if (ev.action == MotionEvent.ACTION_MOVE && isBeingDragged) return true
    when (ev.action) {
      MotionEvent.ACTION_DOWN -> {
        latestDownY = ev.y
      }
      MotionEvent.ACTION_MOVE -> {
        val dy = ev.y - latestDownY
        if (dy < 0) return false
        isBeingDragged = dy > ViewConfiguration.get(context).scaledTouchSlop
      }
      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
        isBeingDragged = false
      }
    }
    return isBeingDragged
  }
  
  override fun onTouchEvent(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    event: MotionEvent
  ): Boolean {
//    if (isPlankOpened || !allowPulling()) return false
//    when (event.action) {
//      MotionEvent.ACTION_DOWN -> {
//        latestDownY = event.y
//        return true
//      }
//      MotionEvent.ACTION_MOVE -> {
//        val dy = event.y - latestDownY
//        var interpolatedValue = interpolate(dy)
//        if (interpolatedValue.isNaN()) {
//          interpolatedValue = 0f
//        }
//        child.updateDistanceToTop(interpolatedValue)
//        distanceToTop = interpolatedValue
//        if (distanceToTop > initialDistanceToTop * 2 + plankHeight
//            && textReleaseToRefreshAlpha != 255) {
//          textPullToRefreshAlpha = 0
//          textReleaseToRefreshAlpha = 255
//          changeTextAnimator.setFloatValues(arrowDownDrawableScale, -1f)
//          changeTextAnimator.start()
//        } else if (distanceToTop < initialDistanceToTop * 2 + plankHeight
//            && textPullToRefreshAlpha != 255) {
//          textPullToRefreshAlpha = 255
//          textReleaseToRefreshAlpha = 0
//          changeTextAnimator.setFloatValues(arrowDownDrawableScale, 1f)
//          changeTextAnimator.start()
//        }
//        invalidate()
//      }
//      MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//        if (distanceToTop < initialDistanceToTop * 2 + plankHeight) {
//          isPlankOpened = false
//          plankAnimator.setFloatValues(distanceToTop, 0f)
//          plankAnimator.doOnEnd { arrowDownDrawable.alpha = 255 }
//          plankAnimator.start()
//        } else {
//          child.animateToRefreshingState()
//          isPlankOpened = true
//        }
//      }
//    }
    return super.onTouchEvent(parent, child, event)
  }
  
  private fun interpolate(distance: Float): Float {
    return sqrt(distance) * 9.5f
  }
}