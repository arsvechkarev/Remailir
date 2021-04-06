package com.arsvechkarev.views

import android.content.Context
import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

abstract class CoroutineView(context: Context) : View(context) {
  
  @PublishedApi
  internal val coroutineScope = CoroutineScope(Dispatchers.Main)
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    coroutineScope.coroutineContext.cancelChildren()
  }
}

inline fun <T : CoroutineView> T.applyOnCoroutine(crossinline block: suspend T.() -> Unit) {
  coroutineScope.launch { block(this@applyOnCoroutine) }
}