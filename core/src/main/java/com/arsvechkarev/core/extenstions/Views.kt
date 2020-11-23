package com.arsvechkarev.core.extenstions

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

operator fun View.contains(event: MotionEvent): Boolean {
  val x = event.x
  val y = event.y
  return x >= left + translationX
      && y >= top + translationY
      && x <= right + translationX
      && y <= bottom + translationY
}

infix fun MotionEvent.happenedIn(view: View): Boolean {
  return x >= 0 && y >= 0 && x <= view.width && y <= view.height
}

fun View.heightWithMargins(): Int {
  val params = layoutParams as? ViewGroup.MarginLayoutParams
  return measuredHeight + (params?.topMargin ?: 0) + (params?.bottomMargin ?: 0)
}

fun EditText.onTextChanged(block: (String) -> Unit) {
  addTextChangedListener(object : TextWatcher {
  
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }
  
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }
  
    override fun afterTextChanged(s: Editable) {
      block(s.toString())
    }
  })
}

fun RecyclerView.allowRecyclerScrolling(): Boolean {
  adapter.ifNotNull { adapter ->
    var pos = -1
    for (i in 0 until childCount) {
      val view = getChildAt(i)
      if (view.bottom > height) {
        pos = i
      }
    }
    if (pos == -1) {
      return false
    }
    return pos < adapter.itemCount
  }
  return false
}