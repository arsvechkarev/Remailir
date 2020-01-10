package com.arsvechkarev.users.repository

import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import firebase.Collections
import javax.inject.Inject

class UsersRepository @Inject constructor() {
  
  fun fetchUsers(eventListener: EventListener<QuerySnapshot>) {
    FirebaseFirestore.getInstance().collection(Collections.Users)
      .addSnapshotListener(eventListener)
  }
  
}