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
    view.showLoading()
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
      view.showUsersList(list)
    }
  }
  
  fun showCurrentList() {
    coroutine {
      val list = repository.getUsersList(true)
      view.showUsersList(list)
    }
  }
  
  private var c = 0
  
  fun sendFriendRequest(username: String) {
    coroutine {
      view.showSendingRequest()
      try {
        delay(MIN_NETWORK_DELAY)
        val result = repository.sendFriendRequest(username)
        if (result == SENT) {
          view.showRequestSent()
        } else {
          view.showSendingRequestFailure(result)
        }
      } catch (e: Throwable) {
        view.showSendingRequestFailure(e, username)
      }
    }
  }
  
  private fun performLoadingList(allowUseCache: Boolean) {
    coroutine {
      try {
        val list = repository.getUsersList(allowUseCache)
        view.showUsersList(list)
      } catch (e: Throwable) {
        view.showFailure(e)
      }
    }
  }
}