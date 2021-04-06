package com.arsvechkarev.views.menu

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.arsvechkarev.views.AnimatableCircleIconView
import com.arsvechkarev.views.R
import com.arsvechkarev.views.utils.Paint
import com.arsvechkarev.views.utils.execute
import config.DurationsConfigurator
import core.resources.Colors
import core.resources.TextSizes
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.Ints.dp
import viewdsl.cancelIfRunning
import viewdsl.children
import viewdsl.contains
import viewdsl.exactly
import viewdsl.isOrientationPortrait
import viewdsl.layoutLeftTop
import kotlin.math.abs
import kotlin.math.hypot

class MenuView(context: Context) : ViewGroup(context) {
  
  private val crossBaseSize = 60.dp
  private val crossOpenedSize = 48.dp
  private val crossBasePadding = 16.dp
  private val crossOpenedPadding = 8.dp
  private val itemSize = 70.dp
  private val itemsHorizontalPadding = 12.dp
  private val pTop = 25.dp
  private val pStart = 25.dp
  private val pEnd = 12.dp
  private val pBottom = 12.dp
  private val textSize = TextSizes.H4
  private val backgroundPaint = Paint(Colors.Accent)
  private var latestY = 0f
  private var latestX = 0f
  private var wasDownEventInView = false
  private var maxItemWidth = 0
  private var maxItemHeight = 0
  private val path = Path()
  private var animCoefficient = 0f
  private var opened = false
  private val coefficientAnimator = ValueAnimator().apply {
    duration = DurationsConfigurator.DurationDefault
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      animCoefficient = it.animatedValue as Float
      invalidate()
    }
  }
  
  private val openCloseView get() = getChildAt(0) as AnimatableCircleIconView
  
  val isOpened get() = opened
  
  val firstMenuItem get() = getChildAt(1) as MenuItemView
  val secondMenuItem get() = getChildAt(2) as MenuItemView
  val thirdMenuItem get() = getChildAt(3) as MenuItemView
  val fourthMenuItem get() = getChildAt(4) as MenuItemView
  
  var onMenuOpenClick: () -> Unit = {}
  var onMenuCloseClick: () -> Unit = {}
  
  init {
    clipToPadding = false
    val iconSize = (crossBaseSize * 0.75f).toInt()
    addView(
      AnimatableCircleIconView(
        context, iconSize,
        R.drawable.avd_plus_to_cross,
        R.drawable.avd_cross_to_plus,
        Colors.Accent, Colors.Icon
      )
    )
    val buildMenuItem = { iconRes: Int, titleRes: Int ->
      MenuItemView(context, iconRes, textSize, itemSize, context.getString(titleRes))
    }
    addView(buildMenuItem(R.drawable.ic_friends, R.string.title_friends))
    addView(buildMenuItem(R.drawable.ic_search, R.string.text_search))
    addView(buildMenuItem(R.drawable.ic_settings, R.string.title_settings))
    addView(buildMenuItem(R.drawable.ic_saved_messages, R.string.title_saved_messages))
  }
  
  fun openMenu(animate: Boolean = true) {
    if (opened) return
    opened = true
    openCloseView.switchToFirstDrawable(animate)
    if (animate) {
      coefficientAnimator.cancelIfRunning()
      coefficientAnimator.setFloatValues(animCoefficient, 1f)
      coefficientAnimator.start()
    } else {
      animCoefficient = 1f
      invalidate()
    }
  }
  
  fun closeMenu(animate: Boolean = true) {
    if (!opened) return
    opened = false
    openCloseView.switchToSecondDrawable(animate)
    if (animate) {
      coefficientAnimator.cancelIfRunning()
      coefficientAnimator.setFloatValues(animCoefficient, 0f)
      coefficientAnimator.start()
    } else {
      animCoefficient = 0f
      invalidate()
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    openCloseView.measure(exactly(crossBaseSize), exactly(crossBaseSize))
    children.forEach { child ->
      if (child is MenuItemView) {
        child.measure(widthMeasureSpec, heightMeasureSpec)
        maxItemWidth = maxOf(maxItemWidth, child.measuredWidth)
        maxItemHeight = maxOf(maxItemHeight, child.measuredHeight)
      }
    }
    if (context.isOrientationPortrait) {
      measurePortrait(widthMeasureSpec, heightMeasureSpec)
    } else {
      measureLandscape(widthMeasureSpec, heightMeasureSpec)
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val topOffset = (crossOpenedSize + crossOpenedPadding).toFloat()
    val width = w.toFloat()
    val height = h.toFloat()
    val curveOffset = minOf(width, height) / 3f
    with(path) {
      moveTo(width, topOffset)
      lineTo(curveOffset, topOffset)
      quadTo(0f, topOffset, 0f, curveOffset + topOffset)
      lineTo(0f, height)
      lineTo(width, height)
      close()
    }
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (context.isOrientationPortrait) {
      layoutPortrait()
    } else {
      layoutLandscape()
    }
    val left = width - crossBaseSize - crossBasePadding
    val right = height - crossBaseSize - crossBasePadding
    openCloseView.layoutLeftTop(left, right)
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    val topOffset = crossOpenedSize + crossOpenedPadding
    val pathHeight = height - topOffset
    drawPath(canvas, pathHeight)
    forEachMenuItem {
      alpha = animCoefficient
      isClickable = alpha == 1f
      translationX = (1f - animCoefficient) * width / 3f
      translationY = (1f - animCoefficient) * pathHeight / 3f
    }
    val openCloseViewRange = pathHeight - crossOpenedPadding
    openCloseView.translationY = animCoefficient * -openCloseViewRange
    val endScale = crossOpenedSize.toFloat() / crossBaseSize.toFloat()
    val scale = 1f - animCoefficient * (1f - endScale)
    openCloseView.scaleX = scale
    openCloseView.scaleY = scale
    super.dispatchDraw(canvas)
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    val touchEvent = super.onTouchEvent(event)
    when (event.action) {
      ACTION_DOWN -> {
        wasDownEventInView = event in openCloseView
        latestX = event.x
        latestY = event.y
        return true
      }
      ACTION_UP -> {
        val dx = abs(event.x - latestX)
        val dy = abs(event.y - latestY)
        val dst = hypot(dx, dy)
        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        if (wasDownEventInView && dst < scaledTouchSlop) {
          wasDownEventInView = false
          if (opened) onMenuCloseClick() else onMenuOpenClick()
        }
      }
    }
    return touchEvent
  }
  
  private fun measurePortrait(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = pStart + pEnd +
        maxItemWidth * 2 + itemsHorizontalPadding
    val height = pTop + pBottom +
        crossOpenedSize + crossOpenedPadding + maxItemHeight * 2
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec),
    )
  }
  
  private fun measureLandscape(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = pStart + pEnd +
        maxItemWidth * 4 + itemsHorizontalPadding * 3
    val height = pTop + pBottom +
        crossOpenedSize + crossOpenedPadding + maxItemHeight
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec),
    )
  }
  
  private fun layoutPortrait() {
    val topOffset = crossOpenedSize + crossOpenedPadding
    firstMenuItem.layout(
      pStart,
      topOffset + pTop,
      pStart + maxItemWidth,
      topOffset + pTop + firstMenuItem.measuredHeight
    )
    secondMenuItem.layout(
      width - pEnd - maxItemWidth,
      topOffset + pTop,
      width - pEnd,
      topOffset + pTop + secondMenuItem.measuredHeight
    )
    thirdMenuItem.layout(
      pStart,
      height - pBottom - maxItemHeight,
      pStart + maxItemWidth,
      height - pBottom - maxItemHeight + thirdMenuItem.measuredHeight
    )
    fourthMenuItem.layout(
      width - pEnd - maxItemWidth,
      height - pBottom - maxItemHeight,
      width - pEnd,
      height - pBottom - maxItemHeight + fourthMenuItem.measuredHeight
    )
  }
  
  private fun layoutLandscape() {
    val topOffset = crossOpenedSize + crossOpenedPadding
    var left = pStart
    children.forEach { child ->
      if (child is MenuItemView) {
        child.layout(left, topOffset + pTop, left + maxItemWidth,
          topOffset + pTop + child.measuredHeight)
        left += maxItemWidth + itemsHorizontalPadding
      }
    }
  }
  
  private fun drawPath(canvas: Canvas, pathHeight: Int) {
    canvas.execute {
      canvas.translate(
        (1f - animCoefficient) * width,
        (1f - animCoefficient) * pathHeight
      )
      canvas.drawPath(path, backgroundPaint)
    }
  }
  
  private fun forEachMenuItem(block: View.() -> Unit) {
    firstMenuItem.apply(block)
    secondMenuItem.apply(block)
    thirdMenuItem.apply(block)
    fourthMenuItem.apply(block)
  }
}