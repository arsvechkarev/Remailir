package com.arsvechkarev.core.model

import com.arsvechkarev.core.recycler.DifferentiableItem

data class User(val username: String) : DifferentiableItem {
  
  override val id = username
}

fun List<String>.toUsersList(): List<User> {
  val list = ArrayList<User>()
  forEach { list.add(User(it)) }
  return list
}