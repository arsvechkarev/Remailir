package com.arsvechkarev.home.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.User
import com.arsvechkarev.home.domain.HomeRepository

class HomePresenter(
  private val repository: HomeRepository,
  dispatchers: Dispatchers
) : BasePresenter<HomeView>(dispatchers) {
  
  fun onRefreshPulled() {
    performLoadingUsers()
  }
  
  fun loadUsersWaitingToChat() {
    viewState.showLoadingUsersThatWaitingForChat()
    performLoadingUsers()
  }
  
  fun respondToChatRequest(user: User) {
    coroutine {
      val result = repository.respondToChatRequest(user)
      if (result) {
        viewState.showGoToChat(user)
      } else {
        viewState.showFailedToRespondToChat(user)
      }
    }
  }
  
  private fun performLoadingUsers() {
    coroutine {
      try {
        val users = repository.getUsersWaitingToChat()
        if (users.isEmpty()) {
          viewState.showNobodyWaitingForChat()
        } else {
          viewState.showWaitingToChatList(users)
        }
      } catch (e: Throwable) {
        viewState.showErrorLoadingWaitingToChat(e)
      }
    }
  }
}