package com.arsvechkarev.styles

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment


val Fragment.COLOR_PROGRESS_CIRCLE: Int
  get() {
    return getAttrColor(
      context!!,
      intArrayOf(R.attr.theColorOnSurface)
    )
  }

val Fragment.COLOR_PROGRESS_CIRCLE_BG: Int
  get() {
    return getAttrColor(
      context!!,
      intArrayOf(R.attr.colorRefreshProgressBackground)
    )
  }

fun getAttrColor(context: Context, valuesArray: IntArray): Int {
  val typedArray = context.obtainStyledAttributes(TypedValue().data, valuesArray)
  val color = typedArray.getColor(0, -1)
  typedArray.recycle()
  assert(color != -1)
  return color
}