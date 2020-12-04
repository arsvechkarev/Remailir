package com.arsvechkarev.core.model.messaging

data class Message(
  val id: String,
  val text: String,
  val sender: String,
  val timestamp: Long
)