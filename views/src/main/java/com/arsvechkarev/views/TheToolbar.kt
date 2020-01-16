package com.arsvechkarev.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.widget.doAfterTextChanged
import animation.VectorAnimationMode.SEARCH_AND_CLOSE
import animation.animateAndThen
import animation.doAfterAnimation
import animation.loadAnimation
import animation.loadAnimationAnd
import animation.rotateOnce


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
    textTitle.text = typedArray.getString(R.styleable.TheToolbar_the_toolbar_title) ?: ""
    val hasSearch = typedArray.getBoolean(R.styleable.TheToolbar_the_toolbar_hasSearch, false)
    hasBackImage =
      typedArray.getBoolean(R.styleable.TheToolbar_the_toolbar_hasBackImage, true)
    imageSearch.isGone = !hasSearch
    imageBack.isGone = !hasBackImage
    if (hasSearch) {
      editTextSearch.hint =
        typedArray.getString(R.styleable.TheToolbar_the_toolbar_searchTint) ?: ""
      setViewSwitcherAnimations()
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
  
  fun goToSearchMode(colorToAppear: Int, colorBackground: Int, onEnd: () -> Unit = {}) {
    isInSearchMode = true
    imageBack.rotateOnce()
    waveView.animate(colorToAppear, colorBackground, onEnd)
    imageSearch.animateAndThen(SEARCH_AND_CLOSE) {
      imageSearch.setImageResource(R.drawable.avd_close_to_search)
    }
    setViewSwitcherAnimations()
    viewSwitcher.showNext()
  }
  
  fun goToNormalMode() {
    isInSearchMode = false
    imageBack.rotateOnce()
    viewSwitcher.reset()
    setViewSwitcherAnimations(reversed = true)
    imageSearch.animateAndThen(SEARCH_AND_CLOSE) {
      imageSearch.setImageResource(R.drawable.avd_search_to_close)
    }
    viewSwitcher.showNext()
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
  
  //TODO (1/16/2020): set reverse animations
  private fun setViewSwitcherAnimations(reversed: Boolean = false) {
    val animationSlideIn: Animation
    val animationSlideOut: Animation
    if (!reversed) {
      animationSlideIn = loadAnimationAnd(R.anim.slide_in_from_right) {
        doAfterAnimation { if (isInSearchMode) editTextSearch.requestFocus() }
      }
      animationSlideOut = loadAnimation(R.anim.slide_out_to_left)
    } else {
      animationSlideIn = loadAnimationAnd(android.R.anim.slide_in_left) {
        doAfterAnimation { if (isInSearchMode) editTextSearch.requestFocus() }
      }
      animationSlideOut = loadAnimation(android.R.anim.slide_out_right)
    }
    viewSwitcher.inAnimation = animationSlideIn
    viewSwitcher.outAnimation = animationSlideOut
  }
}