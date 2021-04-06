package com.arsvechkarev.home.presentation

import com.arsvechkarev.home.domain.HomeRepository
import core.Dispatchers
import core.model.User
import core.ui.BasePresenter
import navigation.Router
import navigation.ScreenInfo
import navigation.screens.Screens

class HomePresenter(
  private val repository: HomeRepository,
  private val router: Router,
  dispatchers: Dispatchers
) : BasePresenter<HomeView>(dispatchers) {
  
  fun onRefreshPulled() {
    performLoadingUsers()
  }
  
  override fun onFirstViewAttach() {
    viewState.showLoadingUsersWaitingToChat()
    performLoadingUsers()
    
  }
  
  fun respondToChatRequest(user: User) {
    coroutine {
      val result = repository.respondToChatRequest(user)
      if (result) {
        //        viewState.showGoToChat(user)
      } else {
        viewState.showFailedToRespondToChat(user)
      }
    }
  }
  
  fun onOpenMenu() {
    viewState.showOpenMenu()
  }
  
  fun onCloseMenu() {
    viewState.showCloseMenu()
  }
  
  fun goToFriendsScreen() {
    goToScreen(Screens().Friends)
  }
  
  fun goToSearchScreen() {
    goToScreen(Screens().Search)
  }
  
  fun goToSettingsScreen() {
    goToScreen(Screens().Settings)
  }
  
  fun goToSavedMessagesScreen() {
  
  }
  
  private fun goToScreen(screen: ScreenInfo) {
    viewState.showCloseMenu()
    router.goForward(screen)
  }
  
  private fun performLoadingUsers() {
    coroutine {
      try {
        val users = repository.getUsersWaitingToChat()
        if (users.isEmpty()) {
          viewState.showEmptyUsersWaitingToChat()
        } else {
          viewState.showSuccessUsersWaitingToChat(users)
        }
      } catch (e: Throwable) {
        viewState.showErrorUsersWaitingToChat(e)
      }
    }
  }
}