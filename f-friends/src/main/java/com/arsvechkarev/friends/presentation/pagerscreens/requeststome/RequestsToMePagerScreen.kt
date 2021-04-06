package com.arsvechkarev.friends.presentation.pagerscreens.requeststome

import android.content.Context
import android.view.View
import android.widget.Toast
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.pagerscreens.CommonFriendsAdapter
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutLoading
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutNoData
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutSuccess
import core.model.User
import core.ui.ViewPagerScreen
import core.ui.utils.ifTrue
import core.ui.utils.moxyPresenter
import viewdsl.invisible
import viewdsl.visible
import viewdsl.withViewBuilder

class RequestsToMePagerScreen : ViewPagerScreen(), RequestsToMeView {
  
  override fun buildView(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      LayoutSuccess(RecyclerViewId, adapter)
      LayoutLoading(LayoutLoadingId, R.string.text_loading_friend_requests)
      LayoutNoData(LayoutNoDataId, R.string.text_no_friend_requests)
    }
  }
  
  private val adapter = CommonFriendsAdapter { presenter.onUserClicked(it) }
  
  private val presenter by moxyPresenter {
    FriendsComponent.get().provideRequestsToMeScreenPresenter()
  }
  
  override fun onInit() {
    presenter.startLoadingRequestsToMe()
    presenter.startListeningToRequestsToMeChanges()
  }
  
  override fun showLoadingList() {
    showOnly(view(LayoutLoadingId))
  }
  
  override fun showListIsEmpty() {
    showOnly(view(LayoutNoDataId))
  }
  
  override fun showLoadedList(data: List<User>) {
    showOnly(view(RecyclerViewId))
    adapter.submitList(data)
  }
  
  override fun showListChanged(newData: List<User>) {
    adapter.submitList(newData)
  }
  
  override fun showFailureLoadingList(e: Throwable) {
    Toast.makeText(contextNonNull, "error", Toast.LENGTH_LONG).show()
  }
  
  override fun showLoadingAcceptingRequest(user: User) {
    Toast.makeText(contextNonNull, "accepting request $user", Toast.LENGTH_SHORT).show()
  }
  
  override fun showSuccessAcceptingRequest(user: User) {
    Toast.makeText(contextNonNull, "done request $user", Toast.LENGTH_SHORT).show()
  }
  
  override fun showFailureAcceptingRequest(user: User) {
  }
  
  override fun showLoadingDismissingRequest(user: User) {
  }
  
  override fun showSuccessDismissingRequest(user: User) {
  }
  
  override fun showFailureDismissingRequest(user: User) {
  }
  
  private fun showOnly(view: View) {
    view(RecyclerViewId).ifTrue({ it !== view }, { invisible() })
    view(LayoutLoadingId).ifTrue({ it !== view }, { invisible() })
    view(LayoutNoDataId).ifTrue({ it !== view }, { invisible() })
    view.visible()
  }
  
  companion object {
    
    private val RecyclerViewId = View.generateViewId()
    private val LayoutLoadingId = View.generateViewId()
    private val LayoutNoDataId = View.generateViewId()
    private val ShackBarId = View.generateViewId()
  }
}