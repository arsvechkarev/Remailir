package com.arsvechkarev.friends.presentation.pagerscreens.fakescreens

import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMeView
import com.arsvechkarev.testcommon.FakeBaseScreen
import com.arsvechkarev.testcommon.ScreenState.Empty
import com.arsvechkarev.testcommon.ScreenState.Failure
import com.arsvechkarev.testcommon.ScreenState.Loading
import com.arsvechkarev.testcommon.ScreenState.Success
import core.model.User

class FakeRequestsToMeScreen : FakeBaseScreen(), RequestsToMeView {
  
  override fun showRemovedRequest(user: User) {
    val currentRequests = currentSuccessState<List<User>>().data
    updateState(Success(currentRequests - user))
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
  
  override fun showFailureLoadingList(e: Throwable) {
    updateState(Failure(e))
  }
}