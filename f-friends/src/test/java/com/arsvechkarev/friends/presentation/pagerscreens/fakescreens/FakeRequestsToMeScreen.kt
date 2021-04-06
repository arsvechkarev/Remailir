package com.arsvechkarev.friends.presentation.pagerscreens.fakescreens

import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMeView
import com.arsvechkarev.testcommon.FakeBaseScreen
import com.arsvechkarev.testcommon.ScreenState.Empty
import com.arsvechkarev.testcommon.ScreenState.Failure
import com.arsvechkarev.testcommon.ScreenState.Loading
import com.arsvechkarev.testcommon.ScreenState.Success
import core.model.User

class FakeRequestsToMeScreen : FakeBaseScreen(), RequestsToMeView {
  
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
  }
  
  override fun showFailureLoadingList(e: Throwable) {
    updateState(Failure(e))
  }
  
  override fun showLoadingAcceptingRequest(user: User) {
  }
  
  override fun showSuccessAcceptingRequest(user: User) {
    val currentRequests = currentSuccessState<List<User>>().data
    updateState(Success(currentRequests - user))
  }
  
  override fun showFailureAcceptingRequest(user: User) {
  }
  
  override fun showLoadingDismissingRequest(user: User) {
  }
  
  override fun showSuccessDismissingRequest(user: User) {
    val currentRequests = currentSuccessState<List<User>>().data
    updateState(Success(currentRequests - user))
  }
  
  override fun showFailureDismissingRequest(user: User) {
  }
}
