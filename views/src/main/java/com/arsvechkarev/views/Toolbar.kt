package com.arsvechkarev.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.core.viewbuilding.Dimens.DividerMargin
import com.arsvechkarev.core.viewbuilding.Dimens.ToolbarImageSize
import com.arsvechkarev.core.viewbuilding.Dimens.ToolbarMargin
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Size
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.Size.IntSize
import com.arsvechkarev.viewdsl.addView
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.circleRippleBackground
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.statusBarHeight
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize

class Toolbar(context: Context) : ViewGroup(context) {
  
  val title get() = getChildAt(0) as TextView
  val imageBack get() = getChildAt(1) as ImageView
  val divider get() = getChildAt(2)
  
  var takeIntoAccountStatusBar = true
    set(value) {
      field = value
      requestLayout()
    }
  
  var showBackImage = true
    set(value) {
      field = value
      requestLayout()
    }
  
  init {
    addView {
      TextView(context).apply(BoldTextView).apply {
        size(MatchParent, WrapContent)
        textSize(TextSizes.H1)
      }
    }
    addView {
      ImageView(context).apply {
        size(ToolbarImageSize, ToolbarImageSize)
        circleRippleBackground(Colors.Ripple)
        image(R.drawable.ic_back)
      }
    }
    addView {
      View(context).apply {
        size(MatchParent, IntSize(DividerHeight))
        backgroundColor(Colors.Divider)
      }
    }
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.onClick(block)
  }
  
  fun title(titleRes: Int) {
    title.text(titleRes)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    title.measure(widthMeasureSpec, heightMeasureSpec)
    imageBack.measure(exactly(ToolbarImageSize), exactly(ToolbarImageSize))
    val height = statusBarHeight() + ToolbarMargin * 2 +
        DividerHeight + title.measuredHeight
    setMeasuredDimension(
      resolveSize(widthMeasureSpec.size, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (showBackImage) {
      val imageBackTop = height / 2 - ToolbarImageSize / 2 + statusBarHeight() / 2
      imageBack.layout(
        ToolbarMargin, imageBackTop,
        ToolbarMargin + ToolbarImageSize, imageBackTop + ToolbarImageSize
      )
    }
    val titleTop = statusBarHeight() + ToolbarMargin
    val titleLeft = imageBack.right + ToolbarMargin
    title.layout(
      titleLeft, titleTop,
      titleLeft + title.measuredWidth, titleTop + title.measuredHeight
    )
    divider.layout(
      DividerMargin, height - DividerHeight,
      width - DividerMargin, height)
  }
  
  private fun statusBarHeight(): Int {
    return if (takeIntoAccountStatusBar) context.statusBarHeight else 0
  }
}