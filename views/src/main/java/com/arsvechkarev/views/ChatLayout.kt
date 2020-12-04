package com.arsvechkarev.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.extenstions.onTextChanged
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.applyColor
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.circleRippleBackground
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.layoutLeftTop
import com.arsvechkarev.viewdsl.padding
import com.arsvechkarev.viewdsl.setMaxLength
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.unspecified

class ChatLayout(context: Context) : ViewGroup(context) {
  
  private val imageSendSize = 38.dp
  private val imageSendPadding = 8.dp
  private val editTextInnerPadding = 12.dp
  
  private val imageEnabledMode: ImageView.() -> Unit = {
    drawable.applyColor(Colors.Icon)
    isClickable = true
  }
  private val imageDisabledMode: ImageView.() -> Unit = {
    drawable.applyColor(Colors.Disabled)
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
      backgroundColor(Colors.Surface)
    })
    addView(EditText(context).apply {
      setHint(R.string.hint_message)
      textSize(TextSizes.H5)
      textColor(Colors.TextPrimary)
      setHintTextColor(Colors.TextSecondary)
      setMaxLength(500)
      setBackgroundColor(Colors.Transparent)
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
      circleRippleBackground(Colors.Ripple)
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