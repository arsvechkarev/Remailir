package com.arsvechkarev.friends.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.friends.domain.FriendsRepository
import kotlinx.coroutines.delay

class FriendsPresenter(
  private val repository: FriendsRepository,
  dispatchers: Dispatchers
) : BasePresenter<FriendsView>(dispatchers) {
  
  private var currentFriendsType = FriendsType.ALL_FRIENDS
  
  fun loadListOf(type: FriendsType) {
    currentFriendsType = type
    if (!repository.hasCacheFor(type)) {
      updateView { showLoading(type) }
    }
    loadListOfType(type, true)
  }
  
  fun onRefreshPulled() {
    loadListOfType(currentFriendsType, false)
  }
  
  private fun loadListOfType(type: FriendsType, allowUseCache: Boolean) {
    coroutine {
      try {
        if (!repository.hasCacheFor(type)) {
          delay(MIN_NETWORK_DELAY)
        }
        val data = repository.loadListOfType(type, allowUseCache)
        if (data.isFromCache) {
          if (data.value.isEmpty()) {
            updateView { showSwitchedToEmptyView(type) }
          } else {
            updateView { showSwitchedToList(type, data.value) }
          }
        } else {
          if (data.value.isEmpty()) {
            updateView { showNoData(type) }
          } else {
            updateView { showList(type, data.value) }
          }
        }
      } catch (e: Throwable) {
        updateView { showFailure(e) }
      }
    }
  }
}