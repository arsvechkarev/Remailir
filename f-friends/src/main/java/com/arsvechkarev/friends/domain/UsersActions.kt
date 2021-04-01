package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.User

/**
 * Helps in performing action on users.
 *
 * Currently, users in database are stored by their username (username is unique, and it can't be
 * changed), but later implementation could be changed to storing users by some id. Therefore, this
 * interface is created, so that repositories can perform actions on list of users without knowing
 * what is used as identifier
 */
interface UsersActions {
  
  /**
   * Adds [user] to [list] and returns [list] back
   */
  fun add(list: MutableList<String>, user: User): MutableList<String>
  
  /**
   * Removes [user] from [list] and returns [list] back
   */
  fun remove(list: MutableList<String>, user: User): MutableList<String>
}

/**
 * Implementation of [UsersActions] that performs actions on users given their username
 */
object ByUsernameUsersActions : UsersActions {
  
  override fun add(list: MutableList<String>, user: User): MutableList<String> {
    list.add(user.username)
    return list
  }
  
  override fun remove(list: MutableList<String>, user: User): MutableList<String> {
    list.remove(user.username)
    return list
  }
}