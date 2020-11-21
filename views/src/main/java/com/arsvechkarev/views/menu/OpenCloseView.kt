package com.arsvechkarev.views.menu

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.views.R

class OpenCloseView(context: Context, iconSize: Int) : FrameLayout(context) {
  
  private val plusToCross = context.retrieveDrawable(R.drawable.avd_plus_to_cross)
      .apply { colorFilter = PorterDuffColorFilter(Colors.Icon, SRC_ATOP) }
  private val crossToPlus = context.retrieveDrawable(R.drawable.avd_cross_to_plus)
      .apply { colorFilter = PorterDuffColorFilter(Colors.Icon, SRC_ATOP) }
  
  private val animatableView get() = getChildAt(0)
  private val circlePaint = Paint(Colors.Accent)
  
  init {
    addView(View(context).apply {
      size(iconSize, iconSize)
    })
    animatableView.layoutGravity(Gravity.CENTER)
    animatableView.background = plusToCross
  }
  
  fun animateToCross() {
    animatableView.background = plusToCross
    (animatableView.background as AnimatedVectorDrawable).start()
  }
  
  fun animateToPlus() {
    animatableView.background = crossToPlus
    (animatableView.background as AnimatedVectorDrawable).start()
  }
  
  override fun dispatchDraw(canvas: Canvas) {
    canvas.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint)
    super.dispatchDraw(canvas)
  }
}