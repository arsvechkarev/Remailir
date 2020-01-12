package core

/**
 * Either [Result] or nothing
 */
@Suppress("UNCHECKED_CAST")
class MaybeResult<out S>(internal val value: Any?) {
  
  val isNothing = value == null
  
  val isFailure: Boolean
    get() {
      value ?: return false
      val result = value as Result<S>
      return result.isFailure
    }
  
  val isSuccess: Boolean
    get() {
      value ?: return false
      val result = value as Result<S>
      return result.isSuccess
    }
  
  
  val data get() = value as S
  
  val error: Throwable get() = (value as Result<S>).exceptionOrNull()!!
  
  companion object {
    
    /** Creates empty result */
    fun <S> nothing(): MaybeResult<S> = MaybeResult(null)
    
    /** Creates successful non-nul result */
    fun <S> success(value: S): MaybeResult<S> {
      val result = Result.success(value)
      return MaybeResult(result)
    }
    
    /** Creates result that either success or failure */
    fun <S> failure(exception: Throwable): MaybeResult<S> {
      val result = Result.failure<S>(exception)
      return MaybeResult(result)
    }
  }
  
}
