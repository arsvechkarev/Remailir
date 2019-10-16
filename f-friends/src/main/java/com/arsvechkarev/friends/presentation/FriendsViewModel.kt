package com.arsvechkarev.friends.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.base.FireViewModel
import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.friends.repository.FriendsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import javax.inject.Inject

class FriendsViewModel @Inject constructor(
  private val repository: FriendsRepository
) : FireViewModel() {
  
  var friends = MutableLiveData<List<Friend>>()
  var chatStatus = MutableLiveData<ChatStatus>()
  
  fun fetchFriends() {
    repository.fetchFriends(
      EventListener { snapshot, exception ->
      if (exception != null) {
        return@EventListener
      }
        val users = snapshot?.toObjects(Friend::class.java)
        val list = users?.filter { it.id != FirebaseAuth.getInstance().uid!! }
        this@FriendsViewModel.friends.value = list
    })

  }
  
  fun createOneToOneChat(friend: Friend) {
    repository.createOneToOneChat(friend, onSuccess = {
      chatStatus.value = ChatStatus.Success(friend)
    }, onFailure = {
      chatStatus.value = ChatStatus.Failure(it)
    })
  }

}