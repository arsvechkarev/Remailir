package core.util

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.text.HtmlCompat

fun View.onClick(block: () -> Unit) {
  setOnClickListener { block() }
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

fun ViewGroup.inflate(@LayoutRes layoutId: Int): View {
  return LayoutInflater.from(this.context).inflate(layoutId, this, false)
}

fun EditText.string(): String = text.toString()

fun TextView.showHtml(text: String) {
  this.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
}