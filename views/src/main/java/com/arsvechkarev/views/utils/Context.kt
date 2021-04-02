package com.arsvechkarev.views.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat


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

@SuppressLint("MissingPermission")
fun Context.vibrate(millis: Long) {
  val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java)!!
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    val effect = VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE)
    vibrator.vibrate(effect)
  } else {
    @Suppress("DEPRECATION")
    vibrator.vibrate(millis)
  }
}