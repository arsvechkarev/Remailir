package com.arsvechkarev.users.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.OtherUser
import core.model.users.User
import core.model.users.toOthers
import firebase.SnapshotFireRepository
import firebase.SnapshotListener
import firebase.schema.Collections
import javax.inject.Inject

class UsersRepository @Inject constructor(
  private val firestore: FirebaseFirestore
) : SnapshotFireRepository<List<OtherUser>>() {
  
  fun fetchUsers(block: SnapshotListener<List<OtherUser>>.() -> Unit) {
    listener = SnapshotListener<List<OtherUser>>().apply(block)
    registration {
      firestore.collection(Collections.Users)
        .addSnapshotListener { snapshot, exception ->
          if (exception != null) {
            listener?.failure?.invoke(exception)
          } else {
            val users = snapshot?.toObjects(User::class.java)
            val otherUsers = users!!
              .filter { it.id != FirebaseAuth.getInstance().uid!! }
              .toOthers()
            listener?.success?.invoke(otherUsers)
          }
        }
    }
  }
  
  
}