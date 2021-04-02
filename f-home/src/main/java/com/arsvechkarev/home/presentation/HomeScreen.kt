package com.arsvechkarev.home.presentation

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.views.ErrorLayout
import com.arsvechkarev.views.LayoutError
import com.arsvechkarev.home.R
import com.arsvechkarev.views.ComplexProgressBar
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
import core.ui.navigation.Screen
import core.ui.utils.ifTrue
import timber.log.Timber
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddings
import viewdsl.tag
import viewdsl.text
import viewdsl.textSize

class HomeScreen : Screen(), HomeView {
  
  @SuppressLint("RtlHardcoded")
  override fun buildLayout() = withViewBuilder {
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
        classNameTag()
        layoutGravity(Gravity.BOTTOM or Gravity.RIGHT)
        firstMenuItem.onClick { closeMenu(); navigator.goToFriendsScreen() }
        secondMenuItem.onClick { closeMenu(); navigator.goToSearchScreen() }
        thirdMenuItem.onClick { closeMenu(); navigator.goToSettingsScreen() }
        fourthMenuItem.onClick { closeMenu(); navigator.goToSavedMessagesScreen() }
      }
    }
  }
  
  //  private val adapter = HomeAdapter(onUserClicked = {
  //    presenter.respondToChatRequest(it)
  //  })
  
  //  private val presenter by moxyPresenter {
  //    HomePresenter(
  //      HomeRepository(
  //        ChatFirebaseDataSource(
  //          core.impl.firebase.FirebaseAuthenticator.getUsername(),
  //          core.impl.AndroidDispatchers
  //        )
  //      ),
  //      core.impl.AndroidDispatchers
  //    )
  //  }
  
  override fun onInit() {
    println()
    //    presenter.loadUsersWaitingToChat()
  }
  
  override fun showLoadingUsersThatWaitingForChat() {
    Timber.d("Loading")
    showLayout(viewAs<RecyclerView>())
    textView("Text").text("Loading users that waiting")
  }
  
  override fun showWaitingToChatList(list: List<User>) {
    textView("Text").text("Loaded")
    showLayout(viewAs<RecyclerView>())
    //    adapter.submitList(list)
  }
  
  override fun showNobodyWaitingForChat() {
    textView("Text").text("Nothing")
    showLayout(viewAs<RecyclerView>())
    Timber.d("Nothing")
  }
  
  override fun showErrorLoadingWaitingToChat(e: Throwable) {
    textView("Text").text("Error")
    Timber.d(e)
  }
  
  override fun showGoToChat(user: User) {
    navigator.respondToChatWith(user)
  }
  
  override fun showFailedToRespondToChat(user: User) {
    Timber.d("showFailedToRespondToChat")
  }
  
  override fun onBackPressed(): Boolean {
    val menuView = viewAs<MenuView>()
    if (menuView.isOpened) {
      menuView.closeMenu()
      return true
    }
    return false
  }
  
  private fun showLayout(layout: View) {
    layout.animateVisible()
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutError).ifTrue({ it !== layout }, { animateInvisible() })
    viewAs<PullToRefreshView>().hide()
  }
  
  companion object {
    
    const val LayoutLoading = "LayoutLoading"
    const val TextLoading = "TextLoading"
  }
}