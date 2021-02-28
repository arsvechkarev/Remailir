package com.arsvechkarev.views

import android.content.Context
import android.view.ViewGroup
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.children
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.size

class SettingsThreeElementsView(context: Context) : ViewGroup(context) {
  
  private var maxItemWidth = 0
  private var maxItemHeight = 0
  
  init {
    val buildMenuItem = { iconRes: Int, titleRes: Int, color: Int ->
      TextWithImageView(context, iconRes, TextSizes.H4, color, context.getString(titleRes))
    }
    addView(buildMenuItem(R.drawable.ic_log_out, R.string.text_log_out, Colors.Error))
    addView(buildMenuItem(R.drawable.ic_share, R.string.text_share, Colors.Share))
    addView(buildMenuItem(R.drawable.ic_source_code, R.string.text_source_code, Colors.SourceCode))
  }
  
  fun onLogOutClick(block: () -> Unit) {
    getChildAt(0).onClick(block)
  }
  
  fun onShareClick(block: () -> Unit) {
    getChildAt(1).onClick(block)
  }
  
  fun onSourceCodeClick(block: () -> Unit) {
    getChildAt(2).onClick(block)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    children.forEach { child ->
      child.measure(widthMeasureSpec, heightMeasureSpec)
      maxItemWidth = maxOf(maxItemWidth, child.measuredWidth)
      maxItemHeight = maxOf(maxItemHeight, child.measuredHeight)
    }
    setMeasuredDimension(
      resolveSize(widthMeasureSpec.size, widthMeasureSpec),
      resolveSize(maxItemHeight, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val padding = (width - maxItemWidth * 3) / 4
    getChildAt(0).apply {
      layout(padding, 0, padding + maxItemWidth, measuredHeight)
    }
    getChildAt(1).apply {
      val left = getChildAt(0).right + padding
      layout(left, 0, left + maxItemWidth, measuredHeight)
    }
    getChildAt(2).apply {
      val left = getChildAt(1).right + padding
      layout(left, 0, left + maxItemWidth, measuredHeight)
    }
  }
}