package com.arsvechkarev.firebase

object Schema {
  
  object Collections {
    const val UsersMetadata = "UsersMetadata"
    const val Users = "Users"
    const val ChatRooms = "ChatRooms"
    const val OneToOneChats = "OneToOneChats"
    const val Messages = "Messages"
  }
  
  object MessageModel {
    const val timestamp = "timestamp"
  }
  
  const val DEFAULT_IMG_URL = "NONE"
  
}