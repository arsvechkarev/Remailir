package com.arsvechkarev.friends.presentation

import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.ifTrue
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Dimens.ProgressBarSizeBig
import com.arsvechkarev.core.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.database.FirebaseUserInfoDatabase
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.list.FriendsAdapter
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.behavior
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.isVisible
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.paddingHorizontal
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

class FriendsScreen : Screen(), FriendsView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
      val headerBehavior = HeaderBehavior()
      child<Toolbar>(MatchParent, WrapContent) {
        classNameTag()
        behavior(headerBehavior)
        showBackImage = true
        title(R.string.title_friends)
        onBackClick { navigator.onBackPress() }
      }
      child<RecyclerView>(MatchParent, MatchParent) {
        classNameTag()
        invisible()
        paddings(top = 16.dp)
        behavior(ScrollingRecyclerBehavior())
        layoutManager = LinearLayoutManager(context)
        this.adapter = this@FriendsScreen.adapter
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutLoading)
        invisible()
        behavior(viewUnderHeaderBehavior)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          gravity(CENTER)
          textSize(TextSizes.H3)
          text("Loading friends...")
        }
        child<ComplexProgressBar>(ProgressBarSizeBig, ProgressBarSizeBig) {
          margins(top = 40.dp)
        }
      }
      FrameLayout(MatchParent, WrapContent) {
        invisible()
        tag(LayoutFailure)
        TextView(WrapContent, WrapContent) {
          layoutGravity(CENTER)
          text("Qwerty")
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutNoFriends)
        invisible()
        behavior(viewUnderHeaderBehavior)
        paddingHorizontal(40.dp)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          gravity(CENTER)
          textSize(TextSizes.H1)
          text("You don't have any friends yet!")
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          gravity(CENTER)
          textSize(TextSizes.H4)
          margins(top = 32.dp, bottom = 32.dp)
          text("Add people to friends so that you can chat with them")
        }
        TextView(WrapContent, WrapContent, style = ClickableButton()) {
          text("Find friends")
          onClick { viewAs<PullToRefreshView>().hide() }
        }
      }
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          if (view(LayoutNoFriends).isVisible) return@lb true
          if (view(LayoutFailure).isVisible) return@lb true
          if (viewAs<RecyclerView>().isVisible
              && headerBehavior.getTopAndBottomOffset() == 0) return@lb true
          return@lb false
        }
      }
    }
  }
  
  private val presenter by moxyPresenter {
    FriendsPresenter(
      FriendsInteractor(
        FirebaseAuthenticator,
        FirebaseUserInfoDatabase(AndroidDispatchers)
      ),
      AndroidDispatchers
    )
  }
  
  private val adapter = FriendsAdapter()
  
  override fun onInit() {
    presenter.startLoadingList()
    getScrollY()
  }
  
  private fun getScrollY() {
    viewNonNull.postDelayed({ getScrollY() }, 1000)
  }
  
  override fun showLoading() {
    println("llll: showLoading")
    showLayout(view(LayoutLoading))
  }
  
  override fun showNoFriends() {
    println("llll: showNoFriends")
    showLayout(view(LayoutNoFriends))
  }
  
  override fun showFriendsList(list: List<User>) {
    println("llll: showFriendsList")
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
  }
  
  override fun showFailure(e: Throwable) {
    println("llll: showFailure")
    showLayout(view(LayoutFailure))
  }
  
  private fun showLayout(layout: View) {
    layout.animateVisible()
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutNoFriends).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutFailure).ifTrue({ it !== layout }, { animateInvisible() })
    viewAs<PullToRefreshView>().hide()
  }
  
  companion object {
    
    const val LayoutLoading = "LayoutLoading"
    const val LayoutFailure = "LayoutFailure"
    const val LayoutNoFriends = "LayoutNoFriends"
  }
}