package com.arsvechkarev.users.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.users.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import core.base.FireViewModel
import core.model.users.OtherUser
import core.model.users.User
import core.model.users.toOthers
import javax.inject.Inject

class UsersViewModel @Inject constructor(
  private val repository: UsersRepository
) : FireViewModel() {
  
  var friends = MutableLiveData<List<OtherUser>>()
  
  fun fetchUsers() {
    repository.fetchUsers(
      EventListener { snapshot, exception ->
        if (exception != null) {
          return@EventListener
        }
        val users = snapshot?.toObjects(User::class.java)
        val otherUsers = users!!
          .filter { it.id != FirebaseAuth.getInstance().uid!! }
          .toOthers()
        this@UsersViewModel.friends.value = otherUsers
      })
  }
  
}