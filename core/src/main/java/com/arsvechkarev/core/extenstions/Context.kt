package com.arsvechkarev.core.extenstions

import android.content.Context
import android.net.ConnectivityManager

val Context.connectivityManager: ConnectivityManager
  get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

val Context.screenWidth: Int
  get() = resources.displayMetrics.widthPixels

val Context.screenHeight: Int
  get() = resources.displayMetrics.heightPixels