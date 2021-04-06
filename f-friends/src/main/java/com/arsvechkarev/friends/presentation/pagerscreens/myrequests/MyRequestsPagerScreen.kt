package com.arsvechkarev.friends.presentation.pagerscreens.myrequests

import android.content.Context
import android.view.View
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

class MyRequestsPagerScreen : ViewPagerScreen(), MyRequestsView {
  
  override fun buildView(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      LayoutSuccess(RecyclerViewId, adapter)
      LayoutLoading(LayoutLoadingId, R.string.text_loading_my_requests)
      LayoutNoData(LayoutNoDataId, R.string.text_no_my_requests)
    }
  }
  
  private val adapter = CommonFriendsAdapter { presenter.onUserClicked(it) }
  
  private val presenter by moxyPresenter {
    FriendsComponent.get().provideMyRequestsScreenPresenter()
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
  }
  
  override fun showLoadingCancelMyRequest(user: User) {
  }
  
  override fun showSuccessCancelMyRequest(user: User) {
  }
  
  override fun showFailureCancelMyRequest(user: User) {
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
  }
}