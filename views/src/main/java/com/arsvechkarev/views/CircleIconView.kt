package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.size

class CircleIconView(
  context: Context,
  size: Int,
  drawableRes1: Int,
  drawableRes2: Int,
  circleColor: Int,
  iconColor: Int,
) : FrameLayout(context) {
  
  private val drawable1 = context.retrieveDrawable(drawableRes1)
      .apply { colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP) }
  private val drawable2 = context.retrieveDrawable(drawableRes2)
      .apply { colorFilter = PorterDuffColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP) }
  
  private val animatableView get() = getChildAt(0)
  private val circlePaint = Paint(circleColor)
  
  init {
    addView(View(context).apply {
      size(size, size)
    })
    animatableView.layoutGravity(Gravity.CENTER)
    animatableView.background = drawable1
  }
  
  fun animateToFirstDrawable() {
    animatableView.background = drawable1
    (animatableView.background as AnimatedVectorDrawable).start()
  }
  
  fun animateToSecondDrawable() {
    animatableView.background = drawable2
    (animatableView.background as AnimatedVectorDrawable).start()
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint)
    super.dispatchDraw(canvas)
  }
}