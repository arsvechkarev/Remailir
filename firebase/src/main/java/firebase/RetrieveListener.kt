package firebase

class RetrieveListener<S> {
  
  lateinit var success: (S) -> Unit
  lateinit var failure: (Throwable) -> Unit
  
  fun onSuccess(successBlock: (S) -> Unit) {
    this.success = successBlock
  }
  
  fun onFailure(failureBlock: (Throwable) -> Unit) {
    this.failure = failureBlock
  }
  
}