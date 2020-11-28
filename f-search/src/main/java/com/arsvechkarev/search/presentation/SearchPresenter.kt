package com.arsvechkarev.search.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.search.domain.RequestResult.SENT
import com.arsvechkarev.search.domain.SearchRepository
import kotlinx.coroutines.delay

class SearchPresenter(
  private val repository: SearchRepository,
  dispatchers: Dispatchers
) : BasePresenter<SearchView>(dispatchers) {
  
  fun loadUsersList() {
    viewState.showLoading()
    performLoadingList(true)
  }
  
  fun onRefreshPulled() {
    performLoadingList(false)
  }
  
  fun performFiltering(text: String) {
    coroutine {
      val list = repository.getUsersList(true).filter { user ->
        user.username.startsWith(text, ignoreCase = true)
      }
      viewState.showUsersList(list)
    }
  }
  
  fun showCurrentList() {
    coroutine {
      val list = repository.getUsersList(true)
      viewState.showUsersList(list)
    }
  }
  
  private var c = 0
  
  fun sendFriendRequest(username: String) {
    coroutine {
      viewState.showSendingRequest()
      try {
        delay(MIN_NETWORK_DELAY)
        val result = repository.sendFriendRequest(username)
        if (result == SENT) {
          viewState.showRequestSent()
        } else {
          viewState.showSendingRequestFailure(result)
        }
      } catch (e: Throwable) {
        viewState.showSendingRequestFailure(e, username)
      }
    }
  }
  
  private fun performLoadingList(allowUseCache: Boolean) {
    coroutine {
      try {
        val list = repository.getUsersList(allowUseCache)
        viewState.showUsersList(list)
      } catch (e: Throwable) {
        viewState.showFailure(e)
      }
    }
  }
}