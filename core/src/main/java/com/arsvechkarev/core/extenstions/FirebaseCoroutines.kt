package com.arsvechkarev.core.extenstions

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun DatabaseReference.waitForSingleValueEvent(): DataSnapshot {
  return suspendCancellableCoroutine { continuation ->
    
    val listener = object : ValueEventListener {
      
      override fun onDataChange(snapshot: DataSnapshot) {
        continuation.resumeWith(Result.success(snapshot))
      }
      
      override fun onCancelled(error: DatabaseError) {
        continuation.resumeWith(Result.failure(error.toException()))
      }
    }
    continuation.invokeOnCancellation {
      this.removeEventListener(listener)
    }
    addListenerForSingleValueEvent(listener)
  }
}

suspend fun <T> Task<T>.await(): T? {
  if (isComplete) {
    val e = exception
    return if (e == null) {
      if (isCanceled) {
        throw CancellationException("Task $this was cancelled normally.")
      } else {
        result!!
      }
    } else {
      throw e
    }
  }
  
  return suspendCancellableCoroutine { continuation ->
    addOnCompleteListener {
      val e = exception
      if (e == null) {
        if (isCanceled) {
          continuation.cancel()
        } else {
          continuation.resume(result)
        }
      } else {
        continuation.resumeWithException(e)
      }
    }
  }
}