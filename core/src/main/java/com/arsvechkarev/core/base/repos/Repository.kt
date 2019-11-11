package com.arsvechkarev.core.base.repos

class ListenersHolder<S> {
  
  lateinit var successBlock: (S) -> Unit
  lateinit var failureBlock: (Throwable) -> Unit
  
  fun onSuccess(successBlock: (S) -> Unit) {
    this.successBlock = successBlock
  }
  
  fun onFailure(failureBlock: (Throwable) -> Unit) {
    this.failureBlock = failureBlock
  }
  
}