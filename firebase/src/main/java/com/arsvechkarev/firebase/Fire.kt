package com.arsvechkarev.firebase

import com.arsvechkarev.core.model.Friend

object Fire {
  
  fun chatIdWith(friend: Friend): String {
    return organizeIdsForChat(thisUserId, friend.id)
  }
  
  fun idOfGroup(list: List<Friend>):String {
    TODO("Do I really need it?")
  }
}