package com.arsvechkarev.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout


class SearchToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  val editTextSearch: EditText by lazy { findViewById<EditText>(R.id.editTextSearch) }
  private val imageBack: ImageView by lazy { findViewById<ImageView>(R.id.imageBack) }
  private val imageCross: ImageView by lazy { findViewById<ImageView>(R.id.imageCross) }
  
  init {
    inflate(context, R.layout.search_toolbar, this)
    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SearchToolbar, 0, 0)
    this.setBackgroundIfNeeded(typedArray, R.styleable.SearchToolbar_search_toolbar_backgroundColor)
    imageBack.setBackgroundIfNeeded(
      typedArray,
      R.styleable.SearchToolbar_search_toolbar_imageBackColor
    )
    imageCross.setBackgroundIfNeeded(
      typedArray,
      R.styleable.SearchToolbar_search_toolbar_imageCrossColor
    )
    editTextSearch.setHintIfNeeded(
      typedArray,
      R.styleable.SearchToolbar_search_toolbar_editTextHint
    )
    maxHeight = getAttributeValue(android.R.attr.actionBarSize)
    minHeight = getAttributeValue(android.R.attr.actionBarSize)
    setConstrains()
  }
  
  private fun setConstrains() {
    imageBack.constraints {
      startToStart = id
      topToTop = id
      bottomToBottom = id
    }
    editTextSearch.constraints {
      startToEnd = R.id.imageBack
      topToTop = id
      endToStart = R.id.imageCross
      bottomToBottom = id
    }
    imageCross.constraints {
      startToEnd = R.id.editTextSearch
      topToTop = id
      endToEnd = id
      bottomToBottom = id
    }
    requestLayout()
  }
  
  fun onBackClick(block: () -> Unit) {
    imageBack.setOnClickListener { block() }
  }
  
  fun onCrossClick(block: () -> Unit) {
    imageCross.setOnClickListener { block() }
  }
  
  fun hideKeyboard(context: Context) {
    val inputMethodManager =
      context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    inputMethodManager!!.hideSoftInputFromWindow(editTextSearch.windowToken, 0)
  }
  
}