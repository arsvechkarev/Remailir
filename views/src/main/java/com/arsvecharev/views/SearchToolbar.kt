package com.arsvecharev.views

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import views.R


class SearchToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
  
  val editTextSearch by lazy { findViewById<EditText>(R.id.editTextSearch) }
  private val imageBack by lazy { findViewById<ImageView>(R.id.imageBack) }
  private val imageCross by lazy { findViewById<ImageView>(R.id.imageCross) }
  
  init {
    inflate(context, R.layout.the_toolbar, this)
    //    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.SearchToolbar, 0, 0)
    //    try {
    //      this.setBackgroundIfNeeded(typedArray, R.styleable.SearchToolbar_backgroundColor)
    //      maxHeight = getAttributeValue(android.R.attr.actionBarSize)
    //      minHeight = getAttributeValue(android.R.attr.actionBarSize)
    //    } finally {
    //      typedArray.recycle()
    //    }
    setConstrains()
  }
  
  private fun setConstrains() {
  
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