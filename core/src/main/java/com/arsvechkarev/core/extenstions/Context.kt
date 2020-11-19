package com.arsvechkarev.core.extenstions

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

val Context.connectivityManager: ConnectivityManager
  get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.retrieveDrawable(@DrawableRes resId: Int): Drawable {
  return ContextCompat.getDrawable(this, resId)!!
}