package com.arsvechkarev.firebase

fun path(vararg args: String) = buildString {
  for (arg in args) {
    append(arg)
    append("/")
  }
}