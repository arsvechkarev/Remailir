package com.arsvechkarev.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone

class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  private var hasSearch = false
  private var title = ""
  
  private val textTitle by lazy { findViewById<TextView>(R.id.textTitle) }
  private val imageBack by lazy { findViewById<ImageView>(R.id.imageBack) }
  private val imageSearch by lazy { findViewById<ImageView>(R.id.imageSearch) }
  
  init {
    inflate(context, R.layout.the_toolbar, this)
    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TheToolbar, 0, 0)
    try {
      this.setBackgroundIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_backgroundColor)
      imageBack.setBackgroundIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_imageBackColor)
      imageSearch.setBackgroundIfNeeded(
        typedArray,
        R.styleable.TheToolbar_the_toolbar_imageSearchColor
      )
      textTitle.setTextColorIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_titleColor)
      textTitle.setTextSizeIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_titleTextSize)
      title = typedArray.getString(R.styleable.TheToolbar_the_toolbar_title) ?: ""
      hasSearch = typedArray.getBoolean(R.styleable.TheToolbar_the_toolbar_hasSearch, false)
      maxHeight = getAttributeValue(android.R.attr.actionBarSize)
      minHeight = getAttributeValue(android.R.attr.actionBarSize)
    } finally {
      typedArray.recycle()
    }
    textTitle.text = title
    imageSearch.isGone = !hasSearch
    setConstrains()
  }
  
  private fun setConstrains() {
    imageBack.constraints {
      topToTop = id
      bottomToBottom = id
      startToStart = id
    }
    textTitle.constraints {
      topToTop = id
      bottomToBottom = id
      startToEnd = R.id.imageBack
    }
    imageSearch.constraints {
      topToTop = id
      bottomToBottom = id
      endToEnd = id
    }
    requestLayout()
  }
  
  fun setTitle(title: CharSequence) {
    textTitle.text = title
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener { block() }
  }
  
  fun onSearchClick(block: () -> Unit) {
    imageSearch.setOnClickListener { block() }
  }
}