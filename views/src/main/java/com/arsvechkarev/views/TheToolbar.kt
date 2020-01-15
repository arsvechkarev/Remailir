package com.arsvechkarev.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import animation.animateVectorDrawable


class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  private val textTitle: TextView
  private val imageBack: ImageView
  private val imageSearch: ImageView
  private val divider: View
  private val waveView: WaveDrawerView
  private val editTextSearch: EditText
  private val viewSwitcher: ViewSwitcher
  
  private var hasBackImage = true
  
  init {
    inflate(context, R.layout.the_toolbar, this)
  
    textTitle = findViewById(R.id.textTitle)
    imageBack = findViewById(R.id.imageBack)
    imageSearch = findViewById(R.id.imageSearch)
    divider = findViewById(R.id.divider)
    waveView = findViewById(R.id.waveView)
    editTextSearch = findViewById(R.id.editTextSearch)
    viewSwitcher = findViewById(R.id.viewSwitcher)
    
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
      topToTop = R.id.textTitle
      bottomToBottom = R.id.textTitle
      startToStart = id
    }
    viewSwitcher.constraints {
      topToTop = id
      if (hasBackImage) {
        startToEnd = R.id.imageBack
      } else {
        startToStart = id
      }
    }
    imageSearch.constraints {
      topToTop = R.id.textTitle
      bottomToBottom = R.id.textTitle
      endToEnd = id
    }
    divider.constraints {
      topToBottom = R.id.textTitle
      startToStart = id
      endToEnd = id
    }
    waveView.constraints {
      topToTop = id
      bottomToBottom = id
    }
    requestLayout()
  }
  
  fun goToSearchMode(color: Int) {
    waveView.animate(color)
    imageSearch.animateVectorDrawable()
  }
  
  fun reverseWave() {
    waveView.reverse()
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener { block() }
  }
  
  fun onSearchClick(block: () -> Unit) {
    imageSearch.setOnClickListener { block() }
  }
}