package styles

import android.content.Context
import android.util.TypedValue
import android.view.View
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment


val View.COLOR_BACKGROUND: Int
  get() = getColor(R.attr.theColorBackground, context)

val View.COLOR_ACCENT: Int
  get() = getColor(R.attr.theColorAccent, context)

val Fragment.COLOR_BACKGROUND: Int
  get() = getColor(R.attr.theColorBackground, requireContext())

val Fragment.COLOR_ACCENT: Int
  get() = getColor(R.attr.theColorAccent, requireContext())

val Fragment.COLOR_PROGRESS_CIRCLE_BG
  get() = getColor(R.attr.colorRefreshProgressBackground, requireContext())

internal fun getColor(@AttrRes resId: Int, context: Context) =
  getAttrColor(context, intArrayOf(resId))

internal fun getAttrColor(context: Context, valuesArray: IntArray): Int {
  val typedArray = context.obtainStyledAttributes(TypedValue().data, valuesArray)
  val color = typedArray.getColor(0, -1)
  typedArray.recycle()
  assert(color != -1)
  return color
}