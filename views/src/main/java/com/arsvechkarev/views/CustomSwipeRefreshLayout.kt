package com.arsvechkarev.views

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class CustomSwipeRefreshLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null
) : SwipeRefreshLayout(context, attrs) {
  
  init {
    val typedArray =
      context.theme.obtainStyledAttributes(attrs, R.styleable.SixDigitCodeLayout, 0, 0)
    val progressLineColor =
      typedArray.getColor(R.styleable.CustomSwipeToRefreshLayout_progressLineColor, Color.WHITE)
    val circleBackgroundColor =
      typedArray.getColor(R.styleable.CustomSwipeToRefreshLayout_circleBackgroundColor, Color.BLACK)
    setColorSchemeColors(progressLineColor)
    setProgressBackgroundColorSchemeColor(circleBackgroundColor)
  }
  
}