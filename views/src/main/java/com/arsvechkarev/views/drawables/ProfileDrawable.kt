package com.arsvechkarev.views.drawables

import android.content.Context
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import viewdsl.retrieveDrawable
import com.arsvechkarev.views.R
import com.arsvechkarev.views.utils.Paint
import core.resources.Colors

class ProfileDrawable(
  context: Context,
  inverseColors: Boolean = false
) : BaseDrawable() {
  
  private val paint = Paint(
    color = if (inverseColors) Colors.UserIconSecondary else Colors.Surface)
  
  private val profileDrawable = context.retrieveDrawable(R.drawable.ic_profile).apply {
    val color = if (inverseColors) Colors.Surface else Colors.UserIconSecondary
    colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP)
  }
  
  override fun onBoundsChange(bounds: Rect) {
    val inset = bounds.width() / 10
    profileDrawable.setBounds(
      inset, inset, bounds.width() - inset, bounds.height() - inset
    )
  }
  
  override fun draw(canvas: Canvas) {
    val hw = bounds.width() / 2f
    val hh = bounds.height() / 2f
    canvas.drawCircle(hw, hh, minOf(hw, hh), paint)
    profileDrawable.draw(canvas)
  }
}