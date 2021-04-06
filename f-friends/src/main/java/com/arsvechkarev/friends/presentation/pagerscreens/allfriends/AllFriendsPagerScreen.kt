package com.arsvechkarev.friends.presentation.pagerscreens.allfriends

import android.content.Context
import android.view.View
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.pagerscreens.CommonFriendsAdapter
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutFailure
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutLoading
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutNoData
import com.arsvechkarev.friends.presentation.pagerscreens.LayoutSuccess
import com.arsvechkarev.friends.presentation.pagerscreens.SnackBar
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.Snackbar
import com.arsvechkarev.views.applyOnCoroutine
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import config.DurationsConfigurator
import core.model.User
import core.resources.Fonts.SegoeUiBold
import core.ui.ViewPagerScreen
import core.ui.spans.spannable
import core.ui.utils.getMessageRes
import core.ui.utils.makeViewsInvisibleExcept
import core.ui.utils.moxyPresenter
import core.ui.utils.shouldAnimate
import kotlinx.coroutines.delay
import viewdsl.Size.Companion.MatchParent
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.onClick
import viewdsl.text
import viewdsl.withViewBuilder

class AllFriendsPagerScreen : ViewPagerScreen(), AllFriendsView {
  
  override fun buildView(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      LayoutSuccess(RecyclerViewId, adapter)
      LayoutLoading(LayoutLoadingId, R.string.text_loading_friends)
      LayoutNoData(LayoutNoDataId, R.string.text_no_friends)
      LayoutFailure(LayoutFailureId, TextFailureId, TextRetryId)
      SnackBar()
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          return@lb presenter.allowPulling()
          //          if (viewAs<Toolbar>().isInSearchMode) return@lb false
          //          if (view(LayoutError).isVisible) return@lb false
          //          if (viewAs<RecyclerView>().isVisible
          //              && headerBehavior.getTopAndBottomOffset() == 0) return@lb true
          return@lb true
        }
      }
    }
  }
  
  private val adapter = CommonFriendsAdapter { presenter.onUserClicked(it) }
  
  private val presenter by moxyPresenter {
    FriendsComponent.get().provideAllFriendsScreenPresenter()
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
    showOnly(view(LayoutFailureId))
    textView(TextFailureId).text(e.getMessageRes())
    textView(TextRetryId).onClick { presenter.onRetryLoadingAllDataClicked() }
  }
  
  override fun showLoadingRemovingFromFriends(user: User) {
    val animate = shouldAnimate(presenter)
    val text = spannable {
      +getText(R.string.text_loading_removing_friend)
      +user.username.withFont(SegoeUiBold)
    }
    viewAs<Snackbar>().apply {
      switchToLoadingMode(animate, text)
      show(animate)
    }
  }
  
  override fun showSuccessRemovingFromFriends(user: User) {
    val animate = shouldAnimate(presenter)
    val text = spannable {
      +getText(R.string.text_success_removing_friend)
      +user.username.withFont(SegoeUiBold)
    }
    viewAs<Snackbar>().applyOnCoroutine {
      switchToSuccessMode(animate, text, onHideClick = { presenter.onHideSnackbarClicked() })
      if (animate) {
        delay(DurationsConfigurator.DurationMedium)
      }
      hide(animate)
    }
  }
  
  override fun showFailureRemovingFromFriends(user: User, error: Throwable) {
    val animate = shouldAnimate(presenter)
    val text = spannable {
      +getText(R.string.text_failure_removing_friend)
      +user.username.withFont(SegoeUiBold)
    }
    viewAs<Snackbar>().apply {
      switchToFailureMode(
        animate = animate,
        failureText = text,
        onRetryClick = { presenter.onRetryRemovingFromFriendsClicked(user) },
        onHideClick = { presenter.onHideSnackbarClicked() }
      )
    }
  }
  
  override fun hideLayoutRemovingFromFriends() {
    viewAs<Snackbar>().hide(shouldAnimate(presenter))
  }
  
  override fun showNewFriendAdded(updatedFriends: List<User>) {
    adapter.submitList(updatedFriends)
  }
  
  private fun showOnly(layout: View) {
    makeViewsInvisibleExcept(
      shouldAnimate = shouldAnimate(presenter),
      allViews = arrayOf(
        view(RecyclerViewId), view(LayoutLoadingId),
        view(LayoutNoDataId), view(LayoutFailureId)
      ),
      except = layout
    )
  }
  
  companion object {
    
    private val RecyclerViewId = View.generateViewId()
    private val LayoutLoadingId = View.generateViewId()
    private val LayoutNoDataId = View.generateViewId()
    private val LayoutFailureId = View.generateViewId()
    private val TextFailureId = View.generateViewId()
    private val TextRetryId = View.generateViewId()
  }
}
