package com.arsvechkarev.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import core.extensions.visible
import kotlinx.android.synthetic.main.the_toolbar.view.revealView


class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  private val textTitle: TextView
  private val imageBack: ImageView
  private val imageSearch: ImageView
  private val divider: View
  
  private var hasBackImage = true
  
  init {
    inflate(context, R.layout.the_toolbar, this)
  
    textTitle = findViewById(R.id.textTitle)
    imageBack = findViewById(R.id.imageBack)
    imageSearch = findViewById(R.id.imageSearch)
    divider = findViewById(R.id.divider)
    
    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TheToolbar, 0, 0)
    imageBack.setBackgroundIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_imageBackColor)
    imageSearch.setBackgroundIfNeeded(
      typedArray,
      R.styleable.TheToolbar_the_toolbar_imageSearchColor
    )
    textTitle.setTextColorIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_titleColor)
    textTitle.setTextSizeIfNeeded(typedArray, R.styleable.TheToolbar_the_toolbar_titleTextSize)
    textTitle.text = typedArray.getString(R.styleable.TheToolbar_the_toolbar_title) ?: ""
    val hasSearch = typedArray.getBoolean(R.styleable.TheToolbar_the_toolbar_hasSearch, false)
    hasBackImage =
      typedArray.getBoolean(R.styleable.TheToolbar_the_toolbar_hasBackImage, true)
    imageSearch.isGone = !hasSearch
    imageBack.isGone = !hasBackImage
    typedArray.recycle()
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
      if (hasBackImage) {
        startToEnd = R.id.imageBack
      } else {
        startToStart = id
      }
    }
    imageSearch.constraints {
      topToTop = id
      bottomToBottom = id
      endToEnd = id
    }
    divider.constraints {
      topToBottom = R.id.textTitle
      startToStart = id
      endToEnd = id
    }
    revealView.constraints {
      topToTop = id
      bottomToBottom = id
    }
    requestLayout()
  }
  
  fun setTitle(title: CharSequence) {
    textTitle.text = title
  }
  
  fun animateWave() {
    findViewById<View>(R.id.revealView).visible()
    val animator = ViewAnimationUtils.createCircularReveal(
      this,
      width,
      height / 2,
      0f,
      width.toFloat()
    )
    
    animator.start()
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener { block() }
  }
  
  fun onSearchClick(block: () -> Unit) {
    imageSearch.setOnClickListener { block() }
  }
}