package com.arsvechkarev.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.stopIfRunning
import com.arsvechkarev.viewdsl.visible

class CheckmarkView(context: Context) : View(context) {
  
  val drawable get() = background as AnimatedVectorDrawable
  
  init {
    background = context.retrieveDrawable(R.drawable.avd_checkmark)
    background.colorFilter = PorterDuffColorFilter(Colors.Correct, PorterDuff.Mode.SRC_ATOP)
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