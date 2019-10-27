package com.arsvechkarev.users.repository

import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.core.model.messaging.OneToOneChat
import com.arsvechkarev.firebase.Fire
import com.arsvechkarev.firebase.Schema
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class UsersRepository @Inject constructor() {
  
  fun fetchFriends(eventListener: EventListener<QuerySnapshot>) {
    FirebaseFirestore.getInstance().collection(Schema.Users).addSnapshotListener(eventListener)
  }
  
  fun createOneToOneChat(
    otherUser: OtherUser,
    onSuccess: () -> Unit,
    onFailure: (Throwable) -> Unit
  ) {
    val thisUserId = FirebaseAuth.getInstance().uid!!
    val otherUserId = otherUser.id
    val oneToOneChat = OneToOneChat(thisUserId, otherUserId, Timestamp.now().seconds)
    FirebaseFirestore.getInstance().collection(Schema.OneToOneChats)
      .document(Fire.chatIdWith(otherUser))
      .set(oneToOneChat)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener(onFailure)
  }
}