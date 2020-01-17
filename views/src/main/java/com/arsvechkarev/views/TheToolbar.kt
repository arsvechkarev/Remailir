package com.arsvechkarev.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewSwitcher
import androidx.annotation.AnimRes
import androidx.annotation.Keep
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.lifecycle.Lifecycle.Event.ON_START
import androidx.lifecycle.Lifecycle.Event.ON_STOP
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import animation.animateVectorDrawable
import animation.doAfterAnimation
import animation.loadAnimation
import animation.loadAnimationAnd
import animation.rotateOnce
import animation.scaleDown
import log.log
import styles.COLOR_ACCENT
import styles.COLOR_BACKGROUND

/**
 * Enhanced toolbar with support of back image, search and animations
 */
class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), LifecycleObserver {
  
  private var textWatcher: TextWatcher? = null
  
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
      setupViewSwitcher()
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
  
  fun attachLifecycle(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycle.addObserver(this)
  }
  
  @OnLifecycleEvent(ON_START)
  @Keep
  fun init() {
    textWatcher?.let { editTextSearch.addTextChangedListener(it) }
    if (isInSearchMode) {
      editTextSearch.requestFocus()
    }
  }
  
  @OnLifecycleEvent(ON_STOP)
  @Keep
  fun cleanup() {
    textWatcher?.let { editTextSearch.removeTextChangedListener(it) }
  }
  
  fun goToSearchMode(animate: Boolean = true, onEnd: () -> Unit = {}) {
    isInSearchMode = true
    setupViewSwitcher()
    
    if (animate) {
      log { "animating" }
      imageBack.rotateOnce()
      waveView.animate(COLOR_ACCENT, COLOR_BACKGROUND, onEnd = onEnd)
      imageSearch.setImageResource(R.drawable.avd_search_to_close)
      imageSearch.animateVectorDrawable()
      viewSwitcher.showNext()
    } else {
      viewSwitcher.showNext()
      imageSearch.setImageResource(R.drawable.ic_cross)
      waveView.animate(COLOR_ACCENT, COLOR_BACKGROUND, quickly = true, onEnd = onEnd)
    }
  }
  
  fun goToNormalMode() {
    textWatcher?.let { editTextSearch.removeTextChangedListener(it) }
    editTextSearch.text.clear()
    isInSearchMode = false
    imageBack.rotateOnce()
    imageSearch.setImageResource(R.drawable.avd_close_to_search)
    imageSearch.animateVectorDrawable()
    viewSwitcher.reset()
    setupViewSwitcher(reversed = true)
    viewSwitcher.showNext()
    waveView.reverse()
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener {
      if (isInSearchMode) {
        editTextSearch.clearFocus()
        exitSearchSmoothly()
      }
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
  
  fun setTextWatcher(block: (String) -> Unit) {
    textWatcher = object : TextWatcher {
      override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
      override fun afterTextChanged(s: Editable) {
        block(s.toString())
      }
    }
  }
  
  /**
   * Add this scale down animation and setting [R.drawable.ic_search] afterwards to prevent weird
   * bug happening when user clicks to back button too fast or clicking back image from search mode
   * and then while next creating of the view [imageBack] has "cross" icon while should have "search"
   */
  private fun exitSearchSmoothly() {
    imageSearch.scaleDown {
      imageSearch.setImageResource(R.drawable.ic_search)
    }
  }
  
  private fun setupViewSwitcher(reversed: Boolean = false) {
    if (!reversed) {
      setupViewSwitcherAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
    } else {
      setupViewSwitcherAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }
  }
  
  private fun setupViewSwitcherAnimations(@AnimRes slideInRes: Int, @AnimRes slideOutRes: Int) {
    val animationSlideIn = loadAnimationAnd(slideInRes) {
      doAfterAnimation { if (isInSearchMode) editTextSearch.requestFocus() }
    }
    val animationSlideOut = loadAnimation(slideOutRes)
    viewSwitcher.inAnimation = animationSlideIn
    viewSwitcher.outAnimation = animationSlideOut
  }
  
}