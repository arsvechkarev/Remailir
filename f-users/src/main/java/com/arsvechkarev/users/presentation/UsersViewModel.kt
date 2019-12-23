package com.arsvechkarev.users.presentation

import androidx.lifecycle.MutableLiveData
import core.base.FireViewModel
import core.model.users.OtherUser
import com.arsvechkarev.users.repository.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import javax.inject.Inject

class UsersViewModel @Inject constructor(
  private val repository: UsersRepository
) : FireViewModel() {
  
  var friends = MutableLiveData<List<OtherUser>>()
  
  fun fetchUsers() {
    repository.fetchFriends(
      EventListener { snapshot, exception ->
        if (exception != null) {
          return@EventListener
        }
        val users = snapshot?.toObjects(OtherUser::class.java)
        val list = users?.filter { it.id != FirebaseAuth.getInstance().uid!! }
        this@UsersViewModel.friends.value = list
      })
  }
  
}