package com.arsvechkarev.views

import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout

fun View.setBackgroundIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val backgroundColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (backgroundColor != Int.MIN_VALUE) this.setBackgroundColor(backgroundColor)
}

fun Array<View>.setBackgroundIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val backgroundColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (backgroundColor != Int.MIN_VALUE) this.forEach { it.setBackgroundColor(backgroundColor) }
}

fun TextView.setTextColorIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (textColor != Int.MIN_VALUE) this.setTextColor(textColor)
}

fun Array<TextView>.setTextColorIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (textColor != Int.MIN_VALUE) this.forEach { it.setTextColor(textColor) }
}

fun TextView.setTextSizeIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textSize = arr.getDimension(stylableRes, Float.MIN_VALUE)
  if (textSize != Float.MIN_VALUE) this.textSize = textSize
}

fun EditText.setHintIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val hint = arr.getText(stylableRes)
  if (hint != null) this.hint = hint
}

fun View.constraints(block: ConstraintLayout.LayoutParams.() -> Unit) {
  (this.layoutParams as ConstraintLayout.LayoutParams).apply(block)
}


fun View.getAttributeValue(resId: Int): Int {
  val typedArray = context.obtainStyledAttributes(TypedValue().data, intArrayOf(resId))
  val value = typedArray.getDimensionPixelSize(0, -1)
  typedArray.recycle()
  return value
}