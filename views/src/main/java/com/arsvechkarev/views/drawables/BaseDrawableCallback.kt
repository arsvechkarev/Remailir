package com.arsvechkarev.views.drawables

import android.graphics.drawable.Drawable

interface BaseDrawableCallback : Drawable.Callback {
  
  override fun invalidateDrawable(who: Drawable) = Unit
  
  override fun scheduleDrawable(who: Drawable, what: Runnable, whenLong: Long) = Unit
  
  override fun unscheduleDrawable(who: Drawable, what: Runnable) = Unit
}