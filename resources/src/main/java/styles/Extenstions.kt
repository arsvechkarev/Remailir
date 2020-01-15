package styles

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment


val Fragment.COLOR_BACKGROUND: Int
  get() = getColor(R.attr.theColorBackground)

val Fragment.COLOR_ACCENT: Int
  get() = getColor(R.attr.theColorAccent)

val Fragment.COLOR_PROGRESS_CIRCLE: Int
  get() = getColor(R.attr.theColorOnSurface)


val Fragment.COLOR_PROGRESS_CIRCLE_BG
  get() = getColor(R.attr.colorRefreshProgressBackground)

internal fun Fragment.getColor(@AttrRes resId: Int) = getAttrColor(context!!, intArrayOf(resId))

internal fun getAttrColor(context: Context, valuesArray: IntArray): Int {
  val typedArray = context.obtainStyledAttributes(TypedValue().data, valuesArray)
  val color = typedArray.getColor(0, -1)
  typedArray.recycle()
  assert(color != -1)
  return color
}