package com.arsvechkarev.core

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.arsvechkarev.core.concurrency.AndroidThreader

@SuppressLint("MissingPermission")
class ConnectivityObserver(
  private val connectivityManager: ConnectivityManager,
  private var onNetworkAvailable: (() -> Unit)?
) : LifecycleObserver {
  
  private val networkCallback = object : ConnectivityManager.NetworkCallback() {
    
    override fun onAvailable(network: Network) {
      AndroidThreader.onMainThread { onNetworkAvailable?.invoke() }
    }
  }
  
  @OnLifecycleEvent(Lifecycle.Event.ON_START)
  fun onStart() {
    connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
  }
  
  @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
  fun onStop() {
    connectivityManager.unregisterNetworkCallback(networkCallback)
    onNetworkAvailable = null
  }
}