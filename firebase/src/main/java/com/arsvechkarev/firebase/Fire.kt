package com.arsvechkarev.firebase

import com.arsvechkarev.core.model.users.OtherUser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

object Fire {
  
  fun chatIdWith(otherUser: OtherUser): String {
    return organizeIdsForChat(thisUserId, otherUser.id)
  }
  
  fun update(status: OnlineOfflineStatus) {
    val userStatus = UserStatus(defineStatus(status), Timestamp.now().seconds, false)
    FirebaseFirestore.getInstance().collection(Schema.Collections.UsersMetadata)
      .document(thisUser.uid)
      .set(userStatus)
  }
}