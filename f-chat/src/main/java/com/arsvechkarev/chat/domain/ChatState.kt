package com.arsvechkarev.chat.domain

enum class ChatState {
  WAITING_FOR_USER,
  NONE,
  CHATTING,
  OTHER_USER_LEFT
}