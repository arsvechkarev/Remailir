package viewdsl

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun ImageView.image(@DrawableRes resId: Int) {
  setImageResource(resId)
}

fun ImageView.image(drawable: Drawable) {
  setImageDrawable(drawable)
}

fun SwitchCompat.setColors(
  colorThumbEnabled: Int,
  colorThumbDisabled: Int,
  colorTrackEnabled: Int,
  colorTrackDisabled: Int
) {
  thumbTintList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
    intArrayOf(
      colorThumbEnabled,
      colorThumbDisabled
    ))
  trackTintList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
    intArrayOf(
      colorTrackEnabled,
      colorTrackDisabled,
    ))
}

fun RecyclerView.setupWith(adapter: RecyclerView.Adapter<*>) {
  layoutManager = LinearLayoutManager(context)
  this.adapter = adapter
}

fun EditText.setMaxLength(max: Int) {
  val filterArray = arrayOfNulls<InputFilter>(1)
  filterArray[0] = InputFilter.LengthFilter(max)
  filters = filterArray
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