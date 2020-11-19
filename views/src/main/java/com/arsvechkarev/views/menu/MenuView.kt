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
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.contains
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.extenstions.i
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.viewdsl.DURATION_DEFAULT
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.cancelIfRunning
import com.arsvechkarev.viewdsl.children
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.layoutLeftTop
import com.arsvechkarev.views.R
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
    duration = DURATION_DEFAULT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      animCoefficient = it.animatedValue as Float
      invalidate()
    }
  }
  
  private val openCloseView get() = getChildAt(0) as OpenCloseView
  
  val firstMenuItem get() = getChildAt(1) as MenuItemView
  val secondMenuItem get() = getChildAt(2) as MenuItemView
  val thirdMenuItem get() = getChildAt(3) as MenuItemView
  val fourthMenuItem get() = getChildAt(4) as MenuItemView
  
  init {
    clipToPadding = false
    addView(OpenCloseView(context, (crossBaseSize * 0.75f).i))
    val buildMenuItem = { iconRes: Int, titleRes: Int ->
      MenuItemView(context, iconRes, textSize, itemSize, context.getString(titleRes))
    }
    addView(buildMenuItem(R.drawable.ic_friends, R.string.text_friends))
    addView(buildMenuItem(R.drawable.ic_search, R.string.text_search))
    addView(buildMenuItem(R.drawable.ic_settings, R.string.text_settings))
    addView(buildMenuItem(R.drawable.ic_saved_messages, R.string.text_saved_messages))
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    maxItemWidth = 0
    maxItemHeight = 0
    openCloseView.measure(exactly(crossBaseSize), exactly(crossBaseSize))
    children.forEach { child ->
      if (child is MenuItemView) {
        child.measure(widthMeasureSpec, heightMeasureSpec)
        maxItemWidth = maxOf(maxItemWidth, child.measuredWidth)
        maxItemHeight = maxOf(maxItemHeight, child.measuredHeight)
      }
    }
    val width = pStart + pEnd +
        maxItemWidth * 2 + itemsHorizontalPadding
    val height = pTop + pBottom +
        crossOpenedSize + crossOpenedPadding + maxItemHeight * 2
    setMeasuredDimension(
      resolveSize(width, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec),
    )
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    val topOffset = (crossOpenedSize + crossOpenedPadding).f
    val width = w.f
    val height = h.f
    val curveOffset = width / 3f
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
    val endScale = crossOpenedSize.f / crossBaseSize.f
    val scale = 1f - animCoefficient * (1f - endScale)
    openCloseView.scaleX = scale
    openCloseView.scaleY = scale
    super.dispatchDraw(canvas)
  }
  
  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
    val dispatchTouchEvent = super.dispatchTouchEvent(ev)
    when (ev.action) {
      ACTION_DOWN -> {
        wasDownEventInView = ev in openCloseView
        latestX = ev.x
        latestY = ev.y
        return true
      }
      ACTION_UP -> {
        val dx = abs(ev.x - latestX)
        val dy = abs(ev.y - latestY)
        val dst = hypot(dx, dy)
        val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        if (wasDownEventInView && dst < scaledTouchSlop) {
          wasDownEventInView = false
          if (opened) closeMenu() else openMenu()
          opened = !opened
        }
      }
    }
    return dispatchTouchEvent
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
  
  private fun openMenu() {
    openCloseView.animateToCross()
    coefficientAnimator.cancelIfRunning()
    coefficientAnimator.setFloatValues(animCoefficient, 1f)
    coefficientAnimator.start()
  }
  
  private fun closeMenu() {
    openCloseView.animateToPlus()
    coefficientAnimator.cancelIfRunning()
    coefficientAnimator.setFloatValues(animCoefficient, 0f)
    coefficientAnimator.start()
  }
}