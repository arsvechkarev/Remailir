package com.arsvechkarev.core.extenstions

import com.arsvechkarev.core.R
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.TimeoutCancellationException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

fun Throwable.getMessageRes() = when (this) {
  is TimeoutException -> R.string.error_timeout
  is TimeoutCancellationException -> R.string.error_timeout
  is UnknownHostException -> R.string.error_no_connection
  is FirebaseNetworkException -> R.string.error_no_connection
  else -> R.string.error_unknown
}

fun Throwable.getRegistrationMessageRes() = when (this) {
  is TimeoutException -> R.string.error_timeout_short
  is TimeoutCancellationException -> R.string.error_timeout_short
  is UnknownHostException -> R.string.error_no_connection_short
  is FirebaseNetworkException -> R.string.error_no_connection_short
  is FirebaseAuthInvalidCredentialsException -> R.string.error_email_is_invalid
  else -> R.string.error_unknown_short
}