package com.arsvechkarev.core.extenstions

import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resumeWithException

suspend fun DatabaseReference.waitForSingleValueEvent(): DataSnapshot {
  return suspendCancellableCoroutine {
    addListenerForSingleValueEvent(object : ValueEventListener {
  
      override fun onDataChange(snapshot: DataSnapshot) {
        it.resumeWith(Result.success(snapshot))
      }
  
      override fun onCancelled(error: DatabaseError) {
        it.resumeWith(Result.failure(error.toException()))
      }
    })
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
  
  return suspendCancellableCoroutine { cont ->
    addOnCompleteListener {
      val e = exception
      if (e == null) {
        if (isCanceled) {
          cont.cancel()
        } else {
          cont.resumeWith(Result.success(result))
        }
      } else {
        cont.resumeWithException(e)
      }
    }
  }
}