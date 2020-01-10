package firebase

class UploadListener {
  
  lateinit var success: () -> Unit
  lateinit var failure: (Throwable) -> Unit
  
  fun onSuccess(successBlock: () -> Unit) {
    this.success = successBlock
  }
  
  fun onFailure(failureBlock: (Throwable) -> Unit) {
    this.failure = failureBlock
  }
  
}