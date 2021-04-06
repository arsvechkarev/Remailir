package com.arsvechkarev.friends.presentation.pagerscreens.fakescreens

import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsView
import com.arsvechkarev.testcommon.FakeBaseScreen
import com.arsvechkarev.testcommon.ScreenState.Empty
import com.arsvechkarev.testcommon.ScreenState.Failure
import com.arsvechkarev.testcommon.ScreenState.Loading
import com.arsvechkarev.testcommon.ScreenState.Success
import core.model.User

class FakeAllFriendsScreen : FakeBaseScreen(), AllFriendsView {
  
  override fun showNewFriendAdded(updatedFriends: List<User>) {
    val success = currentSuccessState<List<User>>()
    val data = success.data
    val state = Success(data + updatedFriends)
    updateState(state)
  }
  
  override fun showLoadingList() {
    updateState(Loading)
  }
  
  override fun showListIsEmpty() {
    updateState(Empty)
  }
  
  override fun showLoadedList(data: List<User>) {
    updateState(Success(data))
  }
  
  override fun showListChanged(newData: List<User>) {
    updateState(Success(newData))
  }
  
  override fun showFailureLoadingList(e: Throwable) {
    updateState(Failure(e))
  }
  
  override fun showLoadingRemovingFromFriends(user: User) {
  }
  
  override fun showSuccessRemovingFromFriends(user: User) {
  }
  
  override fun showFailureRemovingFromFriends(user: User) {
  }
}