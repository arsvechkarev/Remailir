package core.utils

import android.content.Context
import androidx.collection.SparseArrayCompat
import kotlin.random.Random

fun randomFloat(from: Float, to: Float): Float {
  return Random.nextInt(from.toInt(), to.toInt()).toFloat()
}

inline fun <T> SparseArrayCompat<T>.forEach(block: (T) -> Unit) {
  for (i in 0 until this.size()) {
    block(this[keyAt(i)]!!)
  }
}

fun String.dropAfterLast(string: String): String {
  val i = lastIndexOf(string)
  val lengthLeft = length - i
  return dropLast(lengthLeft)
}

fun Context.readAssetsFile(fileName: String): String = assets.open(fileName).bufferedReader()
    .use { reader -> reader.readText() }