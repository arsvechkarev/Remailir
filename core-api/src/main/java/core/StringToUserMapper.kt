package core

import core.model.User
import javax.inject.Inject

/**
 * Maps string with user username to user object
 */
class StringToUserMapper @Inject constructor() : Mapper<String, User> {
  
  override fun map(value: String): User {
    return User(value)
  }
}