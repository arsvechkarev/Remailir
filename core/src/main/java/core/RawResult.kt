package core

class RawResult(val exception: Throwable? = null) {
  
  companion object {
    fun success() = RawResult(null)
    fun failure(exception: Throwable) = RawResult(exception)
  }
}

fun RawResult.whenFailure(block: (Throwable) -> Unit): RawResult {
  if (exception != null) block(exception)
  return this
}

fun RawResult.whenSuccess(action: () -> Unit): RawResult {
  if (exception == null) action()
  return this
}