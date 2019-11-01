package com.arsvechkarev.users.repository

import com.arsvechkarev.firebase.Collections
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class UsersRepository @Inject constructor() {
  
  fun fetchFriends(eventListener: EventListener<QuerySnapshot>) {
    FirebaseFirestore.getInstance().collection(Collections.Users)
      .addSnapshotListener(eventListener)
  }
  
}