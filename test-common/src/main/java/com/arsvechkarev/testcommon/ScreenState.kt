package com.arsvechkarev.testcommon

sealed class ScreenState {
  
  object None : ScreenState()
  
  object Loading : ScreenState()
  
  object Empty : ScreenState()
  
  data class Success<T>(val data: List<T>) : ScreenState()
  
  data class Failure(val e: Throwable) : ScreenState()
}