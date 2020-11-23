@file:Suppress("ObjectPropertyName")

package com.arsvechkarev.viewdsl

object Floats {
  inline val Int.dp: Float get() = Densities.density * this
}

object Ints {
  inline val Int.dp: Int get() = (Densities.density * this).toInt()
}