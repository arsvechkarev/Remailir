package core.exception

class BitmapIsNullException(
  message: String = "",
  cause: Throwable? = null
) : Exception(message, cause)