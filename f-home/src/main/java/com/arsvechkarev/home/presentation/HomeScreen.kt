package com.arsvechkarev.home.presentation

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.common.ErrorLayout
import com.arsvechkarev.common.LayoutError
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.ifTrue
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatFirebaseDataSource
import com.arsvechkarev.home.R
import com.arsvechkarev.home.domain.HomeRepository
import com.arsvechkarev.home.list.HomeAdapter
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.behavior
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.behaviors.HeaderBehavior
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import com.arsvechkarev.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import com.arsvechkarev.views.menu.MenuView
import timber.log.Timber

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
        adapter = this@HomeScreen.adapter
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutLoading)
        invisible()
        behavior(viewUnderHeaderBehavior)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = Styles.BoldTextView) {
          tag(TextLoading)
          gravity(CENTER)
          textSize(TextSizes.H3)
          text(R.string.text_loading_users)
        }
        child<ComplexProgressBar>(Dimens.ProgressBarSizeBig, Dimens.ProgressBarSizeBig) {
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
        onRefreshPulled = { presenter.onRefreshPulled() }
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
        val text = FirebaseAuthenticator.getUsername()
        backgroundColor(Colors.Background)
        title("Me: $text")
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
  
  private val adapter = HomeAdapter(onUserClicked = {
    presenter.respondToChatRequest(it)
  })
  
  private val presenter by moxyPresenter {
    HomePresenter(
      HomeRepository(
        ChatFirebaseDataSource(
          FirebaseAuthenticator.getUsername(),
          AndroidDispatchers
        )
      ),
      AndroidDispatchers
    )
  }
  
  override fun onInit() {
    presenter.loadUsersWaitingToChat()
  }
  
  override fun showLoadingUsersThatWaitingForChat() {
    Timber.d("Loading")
    showLayout(viewAs<RecyclerView>())
    textView("Text").text("Loading users that waiting")
  }
  
  override fun showWaitingToChatList(list: List<User>) {
    textView("Text").text("Loaded")
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
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