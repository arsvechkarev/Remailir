package com.arsvechkarev.views

import android.content.res.TypedArray
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.constraintlayout.widget.ConstraintLayout

fun Array<View>.setBackgroundIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val backgroundColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (backgroundColor != Int.MIN_VALUE) this.forEach { it.setBackgroundColor(backgroundColor) }
}

fun Array<TextView>.setTextColorIfNeeded(arr: TypedArray, @StyleableRes stylableRes: Int) {
  val textColor = arr.getColor(stylableRes, Int.MIN_VALUE)
  if (textColor != Int.MIN_VALUE) this.forEach { it.setTextColor(textColor) }
}

fun View.constraints(block: ConstraintLayout.LayoutParams.() -> Unit) {
  (this.layoutParams as ConstraintLayout.LayoutParams).apply(block)
}
