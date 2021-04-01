package com.arsvechkarev.core.extenstions

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat

private var vibrator: Vibrator? = null

@SuppressLint("MissingPermission")
fun Context.vibrate(millis: Long) {
  val v = vibrator ?: run {
    vibrator = ContextCompat.getSystemService(this, Vibrator::class.java)!!
    vibrator!!
  }
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val effect = VibrationEffect.createOneShot(millis, DEFAULT_AMPLITUDE)
    v.vibrate(effect)
  } else {
    v.vibrate(millis)
  }
}

fun Context.showKeyboard() {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Context.hideKeyboard(editText: EditText) {
  val inputMethodManager =
      getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Context.readAssetsFile(fileName: String): String = assets.open(fileName).bufferedReader()
    .use { reader -> reader.readText() }

val Context.connectivityManager: ConnectivityManager
  get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.screenWidth: Int
  get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
  get() = resources.displayMetrics.heightPixels