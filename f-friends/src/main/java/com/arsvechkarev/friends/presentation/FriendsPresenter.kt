package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import kotlinx.coroutines.delay

class FriendsPresenter(
  private val interactor: FriendsInteractor,
  dispatchers: Dispatchers
) : BasePresenter<FriendsView>(dispatchers) {
  
  fun startLoadingList() {
    updateView { showLoading() }
    loadFriendsList()
  }
  
  fun onRefreshPulled() {
    loadFriendsList()
  }
  
  private fun loadFriendsList() {
    coroutine {
      try {
        delay(6000)
        val list = interactor.loadFriendsList()
        if (list.isEmpty()) {
          updateView { showNoFriends() }
        } else {
          updateView { showFriendsList(list) }
        }
      } catch (e: Throwable) {
        updateView { showFailure(e) }
      }
    }
  }
}