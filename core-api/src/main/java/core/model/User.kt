package core.model

import recycler.DifferentiableItem

/**
 * Represents general information about any user, whether it is current user in the app, friend,
 * or result from search
 *
 * @param username User's unique username
 */
data class User(val username: String) : DifferentiableItem {
  
  override val id = username
}

fun List<String>.toUsersList(): List<User> {
  val list = ArrayList<User>()
  forEach { list.add(User(it)) }
  return list
}