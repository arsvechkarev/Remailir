package com.arsvechkarev.core.extensions

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
  return LayoutInflater.from(this.context).inflate(layoutId, this, false)
}

fun View.visible() {
  visibility = VISIBLE
}

fun View.invisible() {
  visibility = INVISIBLE
}

fun View.gone() {
  visibility = GONE
}

fun ScrollView.scrollToTop() {
  this.smoothScrollTo(0, 0)
}

fun EditText.onSearchClick(block: () -> Unit) {
  this.setOnEditorActionListener ed@{ _, actionId, _ ->
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
      block()
      return@ed true
    }
    return@ed false
  }
}

fun EditText.string(): String = text.toString()

fun EditText.onTextChanged(block: (String) -> Unit) {
  this.addTextChangedListener(object : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
      block(s.toString())
    }
    
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
  })
}

fun TextView.showHtml(text: String) {
  this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
}