package core.model

/**
 * Information about current user
 */
data class ThisUserInfo(
  val username: String,
  val email: String
)

fun ThisUserInfo.toUser() = User(username)