package core.extensions

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment
import core.R


val Fragment.COLOR_ACCENT: Int
  get() {
    return getAttrColor(context!!, intArrayOf(R.attr.theColorAccent))
  }

val Fragment.COLOR_PRIMARY: Int
  get() {
    return getAttrColor(context!!, intArrayOf(R.attr.theColorPrimary))
  }

fun getAttrColor(context: Context, valuesArray: IntArray): Int {
  val typedArray = context.obtainStyledAttributes(TypedValue().data, valuesArray)
  val color = typedArray.getColor(0, -1)
  typedArray.recycle()
  assert(color != -1)
  return color
}