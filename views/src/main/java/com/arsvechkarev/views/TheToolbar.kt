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
import androidx.core.widget.doAfterTextChanged
import animation.animateVectorDrawable
import animation.doAfterAnimation
import animation.loadAnimation
import animation.loadAnimationAnd


class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  var isInSearchMode: Boolean = false
    private set
  
  val editTextSearch: EditText
  
  private val textTitle: TextView
  private val imageBack: ImageView
  private val imageSearch: ImageView
  private val divider: View
  private val waveView: WaveDrawerView
  private val viewSwitcher: ViewSwitcher
  
  private var hasBackImage = true
  
  init {
    inflate(context, R.layout.the_toolbar, this)
  
    editTextSearch = findViewById(R.id.editTextSearch)
    textTitle = findViewById(R.id.textTitle)
    imageBack = findViewById(R.id.imageBack)
    imageSearch = findViewById(R.id.imageSearch)
    divider = findViewById(R.id.divider)
    waveView = findViewById(R.id.waveView)
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
    if (hasSearch) {
      editTextSearch.hint = "Type country name..."
      val animationSlideIn = loadAnimationAnd(android.R.anim.slide_in_left) {
        doAfterAnimation {
          if (isInSearchMode) {
            editTextSearch.requestFocus()
          }
        }
      }
      val animationSlideOut = loadAnimation(android.R.anim.slide_out_right)
      viewSwitcher.inAnimation = animationSlideIn
      viewSwitcher.outAnimation = animationSlideOut
    }
    typedArray.recycle()
    setConstrains()
  }
  
  private fun setConstrains() {
    imageBack.constraints {
      topToTop = R.id.viewSwitcher
      bottomToBottom = R.id.viewSwitcher
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
      topToTop = R.id.viewSwitcher
      bottomToBottom = R.id.viewSwitcher
      endToEnd = id
    }
    divider.constraints {
      topToBottom = R.id.viewSwitcher
      startToStart = id
      endToEnd = id
    }
    waveView.constraints {
      topToTop = id
      bottomToBottom = id
    }
    requestLayout()
  }
  
  fun goToSearchMode(colorToAppear: Int, colorBackground: Int) {
    isInSearchMode = true
    waveView.animate(colorToAppear, colorBackground)
    imageSearch.animateVectorDrawable()
    viewSwitcher.showNext()
  }
  
  fun goToNormalMode() {
    isInSearchMode = false
    viewSwitcher.showPrevious()
    waveView.reverse()
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener {
      if (isInSearchMode) editTextSearch.clearFocus()
      block()
    }
  }
  
  fun onSearchAction(block: () -> Unit) {
    imageSearch.setOnClickListener {
      if (isInSearchMode) {
        editTextSearch.text.clear()
      } else {
        block()
      }
    }
  }
  
  fun onSearchTextChanged(block: (String) -> Unit) {
    editTextSearch.doAfterTextChanged {
      block(it.toString())
    }
  }
}