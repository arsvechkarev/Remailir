package com.arsvechkarev.views

import android.content.Context
import android.graphics.Canvas
import android.widget.FrameLayout
import com.arsvechkarev.core.MockModeDrawerHolder

class RootView(context: Context) : FrameLayout(context) {
  
  override fun dispatchDraw(canvas: Canvas) {
    super.dispatchDraw(canvas)
    MockModeDrawerHolder.mockModeDrawer.drawLabel(canvas)
  }
}