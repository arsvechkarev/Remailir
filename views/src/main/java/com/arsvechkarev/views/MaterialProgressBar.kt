package com.arsvechkarev.views

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.startIfNotRunning
import com.arsvechkarev.viewdsl.stopIfRunning
import com.arsvechkarev.views.MaterialProgressBar.Thickness.NORMAL
import com.arsvechkarev.views.MaterialProgressBar.Thickness.THICK

class MaterialProgressBar(
  context: Context,
  color: Int,
  thickness: Thickness,
) : View(context) {
  
  private val drawable get() = background as AnimatedVectorDrawable
  
  init {
    background = when (thickness) {
      NORMAL -> context.retrieveDrawable(R.drawable.progress_anim_normal)
      THICK -> context.retrieveDrawable(R.drawable.progress_anim_thick)
    }.apply {
      colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }
  }
  
  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    drawable.setBounds(0, 0, w, h)
  }
  
  override fun onVisibilityChanged(changedView: View, visibility: Int) {
    if (visibility == VISIBLE) {
      drawable.startIfNotRunning()
    } else {
      drawable.stopIfRunning()
    }
  }
  
  override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    drawable.startIfNotRunning()
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    drawable.stopIfRunning()
  }
  
  enum class Thickness {
    NORMAL, THICK
  }
}