package com.arsvechkarev.core

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.viewdsl.childView
import moxy.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {
  
  private val viewsCache = HashMap<String, View>()
  
  @Suppress("UNCHECKED_CAST")
  fun view(tag: String): View {
    if (viewsCache[tag] == null) {
      viewsCache[tag] = window.decorView.childView(tag)
    }
    return viewsCache.getValue(tag)
  }
  
  @Suppress("UNCHECKED_CAST")
  fun <T : View> viewAs(tag: String) = view(tag) as T
  
  fun imageView(tag: String) = viewAs<ImageView>(tag)
  
  fun textView(tag: String) = viewAs<TextView>(tag)
  
  fun editText(tag: String) = viewAs<EditText>(tag)
}