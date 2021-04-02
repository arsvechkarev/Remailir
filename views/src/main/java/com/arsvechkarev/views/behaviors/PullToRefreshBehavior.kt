package com.arsvechkarev.views.behaviors

import android.content.Context
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import viewdsl.hasBehavior
import com.arsvechkarev.views.PullToRefreshView
import kotlin.math.sqrt

class PullToRefreshBehavior(
  private val context: Context,
) : CoordinatorLayout.Behavior<PullToRefreshView>() {
  
  private var isBeingDragged = false
  private var plankHeight = 0f
  private var latestDownY = 0f
  private var distanceToTop = 0f
  
  var allowPulling: () -> Boolean = { false }
  
  override fun layoutDependsOn(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    dependency: View
  ): Boolean {
    return dependency.hasBehavior<HeaderBehavior>()
  }
  
  override fun onDependentViewChanged(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    dependency: View
  ): Boolean {
    child.top = dependency.bottom
    return true
  }
  
  override fun onMeasureChild(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    parentWidthMeasureSpec: Int,
    widthUsed: Int,
    parentHeightMeasureSpec: Int,
    heightUsed: Int
  ): Boolean {
    val header = findHeader(parent)
    val height = parent.measuredHeight - header.measuredHeight
    val heightSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, heightSpec, heightUsed)
    return true
  }
  
  override fun onLayoutChild(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    layoutDirection: Int
  ): Boolean {
    val top = findHeader(parent).bottom
    child.layout(0, top, parent.width, parent.height)
    plankHeight = child.getPlankHeight()
    return true
  }
  
  override fun onInterceptTouchEvent(
    parent: CoordinatorLayout,
    child: PullToRefreshView,
    ev: MotionEvent
  ): Boolean {
    if (child.isPlankOpened || !allowPulling()) return false
    if (ev.pointerCount > 2) return false
    val dy = ev.y - latestDownY
    if (ev.action == ACTION_MOVE
        && isBeingDragged
        && dy > 0
    ) {
      return true
    }
    when (ev.action) {
      ACTION_DOWN -> {
        latestDownY = ev.y
      }
      ACTION_MOVE -> {
        if (dy < 0) return false
        isBeingDragged = dy > ViewConfiguration.get(context).scaledTouchSlop
      }
      ACTION_UP, ACTION_CANCEL -> {
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
    if (child.isPlankOpened || !allowPulling()) return false
    if (event.pointerCount > 2) return false
    when (event.action) {
      ACTION_DOWN -> {
        latestDownY = event.y
        return true
      }
      ACTION_MOVE -> {
        val dy = event.y - latestDownY
        var interpolatedValue = interpolate(dy)
        if (interpolatedValue.isNaN()) {
          interpolatedValue = 0f
        }
        child.distanceToTop = interpolatedValue
        distanceToTop = interpolatedValue
        if (distanceToTop > child.initialDistanceToTop * 2 + plankHeight) {
          child.moveToReleaseToRefreshState()
        } else if (distanceToTop < child.initialDistanceToTop * 2 + plankHeight) {
          child.moveToPullToRefreshState()
        }
        child.invalidate()
      }
      ACTION_UP, ACTION_CANCEL -> {
        isBeingDragged = false
        if (distanceToTop < child.initialDistanceToTop * 2 + plankHeight) {
          child.isPlankOpened = false
          child.animateToHiddenState()
        } else {
          child.isPlankOpened = true
          child.animateToRefreshingState()
        }
      }
    }
    return super.onTouchEvent(parent, child, event)
  }
  
  private fun interpolate(distance: Float): Float {
    return sqrt(distance) * 9.5f
  }
  
  private fun findHeader(parent: CoordinatorLayout): View {
    repeat(parent.childCount) {
      val child = parent.getChildAt(it)
      if (child.hasBehavior<HeaderBehavior>()) {
        return child
      }
    }
    throw IllegalStateException()
  }
}