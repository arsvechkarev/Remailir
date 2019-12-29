package com.arsvecharev.views

import android.content.res.TypedArray
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout

fun View.setBackgroundIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val backgroundColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (backgroundColor != Int.MIN_VALUE) this.setBackgroundColor(backgroundColor)
}

fun TextView.setTextColorIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (textColor != Int.MIN_VALUE) this.setTextColor(textColor)
}

fun TextView.setTextSizeIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textSize = arr.getDimension(stylableRes, Float.MIN_VALUE)
  if (textSize != Float.MIN_VALUE) this.textSize = textSize
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