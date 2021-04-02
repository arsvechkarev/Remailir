package com.arsvechkarev.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import core.resources.TextSizes
import viewdsl.Ints.dp
import viewdsl.applyColor
import viewdsl.backgroundColor
import viewdsl.circleRippleBackground
import viewdsl.exactly
import viewdsl.image
import viewdsl.layoutLeftTop
import viewdsl.onTextChanged
import viewdsl.padding
import viewdsl.setMaxLength
import viewdsl.size
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.unspecified

class ChatLayout(context: Context) : ViewGroup(context) {
  
  private val imageSendSize = 38.dp
  private val imageSendPadding = 8.dp
  private val editTextInnerPadding = 12.dp
  
  private val imageEnabledMode: ImageView.() -> Unit = {
    drawable.applyColor(core.resources.Colors.Icon)
    isClickable = true
  }
  private val imageDisabledMode: ImageView.() -> Unit = {
    drawable.applyColor(core.resources.Colors.Disabled)
    isClickable = false
  }
  
  val toolbar get() = getChildAt(0) as Toolbar
  val recyclerView get() = getChildAt(1) as RecyclerView
  val stubBackgroundView get() = getChildAt(2)
  val editText get() = getChildAt(3) as EditText
  val imageSend get() = getChildAt(4) as ImageView
  
  init {
    addView(Toolbar(context).apply {
      showBackImage = true
    })
    addView(RecyclerView(context).apply {
      val linearLayoutManager = LinearLayoutManager(context)
      linearLayoutManager.reverseLayout = false
      linearLayoutManager.stackFromEnd = true
      layoutManager = linearLayoutManager
    })
    addView(View(context).apply {
      backgroundColor(core.resources.Colors.Surface)
    })
    addView(EditText(context).apply {
      setHint(R.string.hint_message)
      textSize(TextSizes.H5)
      textColor(core.resources.Colors.TextPrimary)
      setHintTextColor(core.resources.Colors.TextSecondary)
      setMaxLength(500)
      setBackgroundColor(core.resources.Colors.Transparent)
      padding(editTextInnerPadding)
      onTextChanged {
        if (it.isBlank()) {
          imageSend.apply(imageDisabledMode)
        } else {
          imageSend.apply(imageEnabledMode)
        }
      }
    })
    addView(ImageView(context).apply {
      image(R.drawable.ic_send)
      padding(imageSendPadding)
      circleRippleBackground(core.resources.Colors.Ripple)
      apply(imageDisabledMode)
    })
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    val width = widthMeasureSpec.size
    val height = heightMeasureSpec.size
    toolbar.measure(exactly(width), unspecified())
    imageSend.measure(exactly(imageSendSize), exactly(imageSendSize))
    editText.measure(exactly(width - imageSendSize - editTextInnerPadding * 2), unspecified())
    recyclerView.measure(exactly(width), exactly(height - toolbar.measuredHeight -
        editText.measuredHeight))
    stubBackgroundView.measure(exactly(width),
      exactly(maxOf(editText.measuredHeight, imageSend.measuredHeight)))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    toolbar.layoutLeftTop(0, 0)
    recyclerView.layoutLeftTop(0, toolbar.bottom)
    stubBackgroundView.layoutLeftTop(0, recyclerView.bottom)
    editText.layoutLeftTop(editTextInnerPadding, recyclerView.bottom)
    imageSend.layoutLeftTop(width - imageSend.measuredWidth - editTextInnerPadding,
      height - editText.measuredHeight / 2 - imageSend.measuredHeight / 2)
  }
}