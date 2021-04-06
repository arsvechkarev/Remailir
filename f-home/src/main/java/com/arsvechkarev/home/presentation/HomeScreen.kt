package com.arsvechkarev.home.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.home.R
import com.arsvechkarev.home.di.HomeComponent
import com.arsvechkarev.home.list.HomeAdapter
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.ErrorLayout
import com.arsvechkarev.views.LayoutError
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.behaviors.HeaderBehavior
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import com.arsvechkarev.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import com.arsvechkarev.views.menu.MenuView
import core.model.User
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles.BoldTextView
import core.resources.TextSizes
import core.ui.utils.ifTrue
import core.ui.utils.moxyPresenter
import core.ui.utils.shouldAnimate
import navigation.BaseScreen
import timber.log.Timber
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.makeInvisible
import viewdsl.makeVisible
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddings
import viewdsl.tag
import viewdsl.text
import viewdsl.textSize
import viewdsl.withViewBuilder

class HomeScreen : BaseScreen(), HomeView {
  
  @SuppressLint("RtlHardcoded")
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
      val headerBehavior = HeaderBehavior()
      clipChildren = false
      child<RecyclerView>(MatchParent, MatchParent) {
        classNameTag()
        invisible()
        paddings(top = 16.dp)
        behavior(ScrollingRecyclerBehavior())
        layoutManager = LinearLayoutManager(context)
        //        adapter = this@HomeScreen.adapter
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutLoading)
        invisible()
        behavior(viewUnderHeaderBehavior)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          tag(TextLoading)
          gravity(CENTER)
          textSize(TextSizes.H3)
          text(R.string.text_loading_users)
        }
        child<ComplexProgressBar>(ProgressBarSizeBig, ProgressBarSizeBig) {
          margins(top = 40.dp)
        }
      }
      ErrorLayout()
      TextView(WrapContent, WrapContent, style = BoldTextView) {
        tag("Text")
        textSize(TextSizes.H2)
        layoutGravity(CENTER)
      }
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        //        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          //          if (viewAs<Toolbar>().isInSearchMode) return@lb false
          //          if (view(LayoutError).isVisible) return@lb false
          //          if (viewAs<RecyclerView>().isVisible
          //              && headerBehavior.getTopAndBottomOffset() == 0) return@lb true
          return@lb true
        }
      }
      child<Toolbar>(MatchParent, WrapContent) {
        classNameTag()
        //        val text = core.impl.firebase.FirebaseAuthenticator.getUsername()
        //        title("Me: $text")
        behavior(headerBehavior)
      }
      child<MenuView>(WrapContent, WrapContent) {
        onMenuOpenClick = { presenter.onOpenMenu() }
        onMenuCloseClick = { presenter.onCloseMenu() }
        classNameTag()
        layoutGravity(Gravity.BOTTOM or Gravity.RIGHT)
        firstMenuItem.onClick { presenter.goToFriendsScreen() }
        secondMenuItem.onClick { presenter.goToSearchScreen() }
        thirdMenuItem.onClick { presenter.goToSettingsScreen() }
        fourthMenuItem.onClick { presenter.goToSavedMessagesScreen() }
      }
    }
  }
  
  private val adapter = HomeAdapter(onUserClicked = {
    presenter.respondToChatRequest(it)
  })
  
  private val presenter by moxyPresenter { HomeComponent.getPresenter() }
  
  override fun showLoadingUsersWaitingToChat() {
    Timber.d("Loading")
    showLayout(viewAs<RecyclerView>())
    textView("Text").text("Loading users that waiting")
  }
  
  override fun showSuccessUsersWaitingToChat(list: List<User>) {
    textView("Text").text("Loaded")
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
  }
  
  override fun showEmptyUsersWaitingToChat() {
    textView("Text").text("Nothing")
    showLayout(viewAs<RecyclerView>())
    Timber.d("Nothing")
  }
  
  override fun showErrorUsersWaitingToChat(e: Throwable) {
    textView("Text").text("Error")
    Timber.d(e)
  }
  
  override fun showFailedToRespondToChat(user: User) {
    Timber.d("showFailedToRespondToChat")
  }
  
  override fun showOpenMenu() {
    viewAs<MenuView>().openMenu(animate = !presenter.isInRestoreState(this))
  }
  
  override fun showCloseMenu() {
    viewAs<MenuView>().closeMenu(animate = !presenter.isInRestoreState(this))
  }
  
  override fun handleBackPress(): Boolean {
    val menuView = viewAs<MenuView>()
    if (menuView.isOpened) {
      menuView.closeMenu()
      return false
    }
    return true
  }
  
  private fun showLayout(layout: View) {
    val animate = shouldAnimate(presenter)
    viewAs<RecyclerView>().ifTrue({ it !== layout }) { makeInvisible(animate) }
    view(LayoutLoading).ifTrue({ it !== layout }) { makeInvisible(animate) }
    view(LayoutError).ifTrue({ it !== layout }) { makeInvisible(animate) }
    viewAs<PullToRefreshView>().hide()
    layout.makeVisible(animate)
  }
  
  companion object {
    
    const val LayoutLoading = "LayoutLoading"
    const val TextLoading = "TextLoading"
  }
}