package com.arsvechkarev.views

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.AnimatedVectorDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.core.extenstions.hideKeyboard
import com.arsvechkarev.core.extenstions.showKeyboard
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Colors.Icon
import com.arsvechkarev.core.viewbuilding.Dimens.DividerHeight
import com.arsvechkarev.core.viewbuilding.Dimens.DividerMargin
import com.arsvechkarev.core.viewbuilding.Dimens.ToolbarImageSize
import com.arsvechkarev.core.viewbuilding.Dimens.ToolbarMargin
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.Size.IntSize
import com.arsvechkarev.viewdsl.animateGone
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.circleRippleBackground
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.layoutLeftTop
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.padding
import com.arsvechkarev.viewdsl.paddingHorizontal
import com.arsvechkarev.viewdsl.retrieveDrawable
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.statusBarHeight
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.unspecified

class Toolbar(context: Context) : ViewGroup(context) {
  
  private val title get() = getChildAt(0) as TextView
  private val imageBack get() = getChildAt(1) as ImageView
  private val editText get() = getChildAt(2) as EditText
  private val imageSearch get() = getChildAt(3) as ImageView
  private val divider get() = getChildAt(4)
  
  private var textWatcher: TextWatcher? = null
  private var searchMode = false
  
  val isInSearchMode get() = searchMode
  var onExitFromSearchMode = {}
  
  var takeIntoAccountStatusBar = true
    set(value) {
      field = value
      requestLayout()
    }
  
  var showBackImage = false
    set(value) {
      field = value
      requestLayout()
    }
  
  var showSearchImage = false
    set(value) {
      field = value
      requestLayout()
    }
  
  init {
    addView(TextView(context).apply(BoldTextView).apply {
      size(MatchParent, WrapContent)
      textSize(TextSizes.H1)
    })
    addView(ImageView(context).apply {
      padding(8.dp)
      size(ToolbarImageSize, ToolbarImageSize)
      circleRippleBackground(Colors.Ripple)
      image(R.drawable.ic_back)
    })
    addView(EditText(context).apply {
      textSize(TextSizes.H4)
      paddingHorizontal(16.dp)
      setBackgroundResource(android.R.color.transparent)
      invisible()
    })
    addView(ImageView(context).apply {
      padding(6.dp)
      size(ToolbarImageSize, ToolbarImageSize)
      circleRippleBackground(Colors.Ripple)
      setupAndSetDrawable(R.drawable.avd_search_to_close)
      invisible()
      onClick {
        if (searchMode) switchFromSearchMode() else switchToSearchMode()
      }
    })
    addView(View(context).apply {
      size(MatchParent, IntSize(DividerHeight))
      backgroundColor(Colors.Divider)
    })
  }
  
  fun onBackClick(block: () -> Unit) {
    showBackImage = true
    imageBack.onClick(block)
  }
  
  fun title(titleRes: Int) {
    title.text(titleRes)
  }
  
  fun onSearchTyped(block: (String) -> Unit) {
    textWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) = Unit
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
      override fun afterTextChanged(s: Editable) {
        block(s.toString())
      }
    }
    editText.addTextChangedListener(textWatcher)
  }
  
  fun animateSearchVisible() {
    imageSearch.animateVisible()
  }
  
  fun animateSearchInvisible() {
    imageSearch.animateGone()
  }
  
  fun handleBackPressed() {
    switchFromSearchMode()
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    title.measure(widthMeasureSpec, heightMeasureSpec)
    val imageBackSize = ToolbarImageSize + imageBack.paddingTop + imageBack.paddingBottom
    imageBack.measure(exactly(imageBackSize), exactly(imageBackSize))
    val imageSearchSize = ToolbarImageSize + imageSearch.paddingTop + imageSearch.paddingBottom
    imageSearch.measure(exactly(imageSearchSize), exactly(imageSearchSize))
    editText.measure(
      exactly(widthMeasureSpec.size - imageSearchSize -
          imageBackSize - ToolbarMargin * 2),
      unspecified()
    )
    val height = statusBarHeight() + ToolbarMargin * 2 +
        DividerHeight + title.measuredHeight
    setMeasuredDimension(
      resolveSize(widthMeasureSpec.size, widthMeasureSpec),
      resolveSize(height, heightMeasureSpec)
    )
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    if (showBackImage) {
      val imageSize = ToolbarImageSize + imageBack.paddingTop + imageBack.paddingBottom
      val imageBackTop = height / 2 - imageSize / 2 + statusBarHeight() / 2
      val left = ToolbarMargin / 2
      imageBack.layout(
        left, imageBackTop, left + imageSize, imageBackTop + imageSize
      )
    }
    if (showSearchImage) {
      val imageSize = ToolbarImageSize + imageSearch.paddingTop + imageSearch.paddingBottom
      val imageSearchTop = height / 2 - imageSize / 2 + statusBarHeight() / 2
      val left = width - imageSearchTop - ToolbarMargin
      imageSearch.layout(
        left, imageSearchTop, left + imageSize, imageSearchTop + imageSize
      )
    }
    val titleLeft = imageBack.right + ToolbarMargin
    val editTextTop = height / 2 - editText.measuredHeight / 2 + statusBarHeight() / 2
    editText.layoutLeftTop(imageBack.right, editTextTop)
    val titleTop = height / 2 - title.measuredHeight / 2 + statusBarHeight() / 2
    title.layoutLeftTop(titleLeft, titleTop)
    divider.layout(
      DividerMargin, height - DividerHeight,
      width - DividerMargin, height)
  }
  
  override fun onDetachedFromWindow() {
    editText.removeTextChangedListener(textWatcher)
    textWatcher = null
    super.onDetachedFromWindow()
  }
  
  private fun switchToSearchMode() {
    if (searchMode) return
    searchMode = true
    editText.requestFocus()
    imageSearch.setupAndSetDrawable(R.drawable.avd_search_to_close)
    (imageSearch.drawable as AnimatedVectorDrawable).start()
    editText.animateVisible(andThen = {
      editText.requestFocus()
      context.showKeyboard()
    })
    title.animateInvisible()
  }
  
  private fun switchFromSearchMode() {
    if (!searchMode) return
    searchMode = false
    onExitFromSearchMode()
    editText.clearAnimation()
    context.hideKeyboard(editText)
    imageSearch.setupAndSetDrawable(R.drawable.avd_close_to_search)
    (imageSearch.drawable as AnimatedVectorDrawable).start()
    editText.animateInvisible()
    title.animateVisible()
  }
  
  private fun statusBarHeight(): Int {
    return if (takeIntoAccountStatusBar) context.statusBarHeight else 0
  }
  
  private fun ImageView.setupAndSetDrawable(drawableRes: Int) {
    setImageDrawable(context.retrieveDrawable(drawableRes).apply {
      colorFilter = PorterDuffColorFilter(Icon, SRC_ATOP)
    })
  }
}