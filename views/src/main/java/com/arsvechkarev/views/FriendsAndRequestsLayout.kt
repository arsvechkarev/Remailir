package com.arsvechkarev.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.ViewConfiguration
import android.view.ViewGroup
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.contains
import com.arsvechkarev.core.extenstions.f
import com.arsvechkarev.core.extenstions.i
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.REQUESTS_TO_ME
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.viewdsl.DURATION_SHORT
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.cancelIfRunning
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.layoutLeftTop
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.string
import com.arsvechkarev.viewdsl.unspecified
import kotlin.math.abs
import kotlin.math.hypot

class FriendsAndRequestsLayout(context: Context) : ViewGroup(context) {
  
  private val arrowIcon get() = getChildAt(0) as CircleIconView
  private val allFriendsChip get() = getChildAt(1) as Chip
  private val friendsRequestsChip get() = getChildAt(2) as Chip
  private val myRequestsChip get() = getChildAt(3) as Chip
  private val chipVerticalPadding = 20.dp
  private val chipHorizontalPadding = 16.dp
  private val arrowIconSize = 60.dp
  private val arrowIconPadding = 16.dp
  private val chipGroup: ChipGroup
  private val backgroundPaint = Paint(Colors.Dialog)
  private var viewsHeight = 0
  private var opened = false
  private var wasDownEventInView = false
  private var latestY = 0f
  private var latestX = 0f
  private val coefficientAnimator = ValueAnimator().apply {
    duration = DURATION_SHORT
    interpolator = AccelerateDecelerateInterpolator
    addUpdateListener {
      translationY = it.animatedValue as Float
    }
  }
  
  val isOpened get() = opened
  
  init {
    clipToPadding = false
    setPadding(20.dp, 20.dp, 20.dp, 20.dp)
    addView(
      CircleIconView(
        context, (arrowIconSize * 0.6f).i,
        R.drawable.avd_up_to_down,
        R.drawable.avd_down_to_up,
        Colors.Dialog, Colors.Icon
      )
    )
    val createChip = { text: String ->
      Chip(context, text, TextSizes.H4, Colors.TextPrimary, Colors.Dialog)
    }
    addView(createChip(string(R.string.text_all_friends)))
    addView(createChip(string(R.string.text_my_requests)))
    addView(createChip(string(R.string.text_friend_requests)))
    chipGroup = ChipGroup(allFriendsChip, friendsRequestsChip, myRequestsChip)
    allFriendsChip.isSelected = true
    arrowIcon.onClick {
      if (isOpened) close() else open()
    }
  }
  
  fun open() {
    if (opened) return
    opened = true
    arrowIcon.animateToFirstDrawable()
    coefficientAnimator.cancelIfRunning()
    coefficientAnimator.setFloatValues(translationY, height.f - viewsHeight)
    coefficientAnimator.start()
  }
  
  fun close() {
    if (!opened) return
    opened = false
    arrowIcon.animateToSecondDrawable()
    coefficientAnimator.cancelIfRunning()
    coefficientAnimator.setFloatValues(translationY, height.f - arrowIconSize - arrowIconPadding)
    coefficientAnimator.start()
  }
  
  fun onClick(block: (type: FriendsType) -> Unit) {
    chipGroup.onNewChipSelected = {
      when (it.text) {
        string(R.string.text_all_friends) -> block(ALL_FRIENDS)
        string(R.string.text_my_requests) -> block(MY_REQUESTS)
        string(R.string.text_friend_requests) -> block(REQUESTS_TO_ME)
        else -> throw IllegalStateException()
      }
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    arrowIcon.measure(exactly(arrowIconSize), exactly(arrowIconSize))
    allFriendsChip.measure(unspecified(), unspecified())
    friendsRequestsChip.measure(unspecified(), unspecified())
    myRequestsChip.measure(unspecified(), unspecified())
    viewsHeight = allFriendsChip.measuredHeight + myRequestsChip.measuredHeight +
        chipVerticalPadding + paddingTop + paddingBottom + arrowIconPadding + arrowIconSize
    setMeasuredDimension(
      widthMeasureSpec.size,
      resolveSize(viewsHeight, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val top = arrowIconPadding + arrowIconSize
    arrowIcon.layoutLeftTop(width - arrowIconSize - arrowIconPadding, 0)
    allFriendsChip.layoutLeftTop(paddingStart, top + paddingTop)
    friendsRequestsChip.layoutLeftTop(allFriendsChip.right + chipHorizontalPadding,
      top + paddingTop)
    myRequestsChip.layoutLeftTop(paddingStart,
      friendsRequestsChip.bottom + chipVerticalPadding)
    translationY = height.f - arrowIconSize - arrowIconPadding
  }
  
  override fun onTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
      ACTION_DOWN -> {
        wasDownEventInView = event in arrowIcon
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
          if (opened) close() else open()
        }
      }
    }
    return super.onTouchEvent(event)
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    val topOffset = (arrowIconPadding + arrowIconSize).toFloat()
    canvas.drawRect(0f, topOffset, width.f, height.f, backgroundPaint)
    super.dispatchDraw(canvas)
  }
}