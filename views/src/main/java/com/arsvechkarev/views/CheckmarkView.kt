package com.arsvechkarev.views

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.view.View
import config.DurationsConfigurator
import core.resources.Colors
import kotlinx.coroutines.delay
import viewdsl.retrieveDrawable
import viewdsl.stopIfRunning
import viewdsl.visible

class CheckmarkView(context: Context) : View(context) {
  
  private val drawable = context.retrieveDrawable(R.drawable.ic_chechmark).apply {
    colorFilter = PorterDuffColorFilter(Colors.Correct, SRC_ATOP)
  }
  private val animatedDrawable = context.retrieveDrawable(R.drawable.avd_checkmark).apply {
    colorFilter = PorterDuffColorFilter(Colors.Correct, SRC_ATOP)
  } as AnimatedVectorDrawable
  
  suspend fun showCheckmarkAndWait(animate: Boolean) {
    visible()
    if (animate) {
      background = animatedDrawable
      animatedDrawable.start()
      delay(DurationsConfigurator.DurationCheckmark)
    } else {
      background = drawable
    }
  }
  
  fun animateCheckmark(andThen: () -> Unit = {}) {
    visible()
    animatedDrawable.start()
    invalidate()
    handler.postDelayed(andThen, DELAY)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    (background as? AnimationDrawable)?.stopIfRunning()
  }
  
  private companion object {
    
    const val DELAY = 1300L
  }
}