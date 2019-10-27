package com.arsvechkarev.firebase

import com.arsvechkarev.core.model.OtherUser

object Fire {
  
  fun chatIdWith(otherUser: OtherUser): String {
    return organizeIdsForChat(thisUserId, otherUser.id)
  }
  
  fun idOfGroup(list: List<OtherUser>):String {
    TODO("Do I really need it?")
  }
}