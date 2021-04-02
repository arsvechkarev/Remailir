package com.arsvechkarev.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import viewdsl.retrieveDrawable
import viewdsl.stopIfRunning
import viewdsl.visible

class CheckmarkView(context: Context) : View(context) {
  
  private val drawable get() = background as AnimatedVectorDrawable
  
  init {
    background = context.retrieveDrawable(R.drawable.avd_checkmark).apply { }
    background.colorFilter = PorterDuffColorFilter(core.resources.Colors.Correct,
      PorterDuff.Mode.SRC_ATOP)
  }
  
  fun animateCheckmark(andThen: () -> Unit = {}) {
    visible()
    drawable.start()
    invalidate()
    handler.postDelayed(andThen, DELAY)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    drawable.stopIfRunning()
  }
  
  private companion object {
    
    const val DELAY = 1300L
  }
}