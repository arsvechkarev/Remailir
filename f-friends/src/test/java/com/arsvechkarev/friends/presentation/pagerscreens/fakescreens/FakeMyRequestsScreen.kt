package com.arsvechkarev.friends.presentation.pagerscreens.fakescreens

import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsView
import com.arsvechkarev.testcommon.FakeBaseScreen
import com.arsvechkarev.testcommon.ScreenState.Empty
import com.arsvechkarev.testcommon.ScreenState.Failure
import com.arsvechkarev.testcommon.ScreenState.Loading
import com.arsvechkarev.testcommon.ScreenState.Success
import core.model.User

class FakeMyRequestsScreen : FakeBaseScreen(), MyRequestsView {
  
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
  
  override fun showLoadingCancelMyRequest(user: User) {
  }
  
  override fun showSuccessCancelMyRequest(user: User) {
    val success = currentSuccessState<List<User>>()
    updateState(Success(success.data - user))
  }
  
  override fun showFailureCancelMyRequest(user: User) {
  }
}
