package com.arsvechkarev.core

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import com.arsvechkarev.core.extenstions.connectivityManager
import timber.log.Timber
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@SuppressLint("MissingPermission")
class NetworkConnectionImpl(context: Context) : NetworkConnection {
  
  private val latch = CountDownLatch(1)
  
  private val callback = object : ConnectivityManager.NetworkCallback() {
    
    override fun onAvailable(network: Network) {
      Timber.d("Connection is available")
      this@NetworkConnectionImpl.isConnected = true
      latch.countDown()
    }
  }
  
  override var isConnected: Boolean = false
    private set
    get() {
      latch.await(1, TimeUnit.SECONDS)
      return field
    }
  
  init {
    context.connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), callback)
  }
}
