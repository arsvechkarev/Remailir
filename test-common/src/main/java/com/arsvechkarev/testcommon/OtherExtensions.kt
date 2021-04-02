package com.arsvechkarev.testcommon

import core.model.User

@OptIn(ExperimentalStdlibApi::class)
fun usersList(vararg names: String): List<User> {
  return buildList { names.forEach { name -> add(User(name)) } }
}

fun user(name: String) = User(name)