package com.arsvechkarev.views.drawables

import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

abstract class BaseDrawable : Drawable() {
  
  override fun setAlpha(alpha: Int) = Unit
  
  override fun setColorFilter(colorFilter: ColorFilter?) = Unit
  
  override fun getOpacity() = PixelFormat.OPAQUE
}