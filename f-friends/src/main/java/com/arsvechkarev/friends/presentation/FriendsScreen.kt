package com.arsvechkarev.friends.presentation

import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.ifTrue
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.FriendsType
import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.FRIENDS_REQUESTS
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
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
import com.arsvechkarev.friends.domain.FriendsRepository
import com.arsvechkarev.friends.list.FriendsAdapter
import com.arsvechkarev.viewdsl.DURATION_SHORT
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.behavior
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.gone
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
import com.arsvechkarev.viewdsl.visible
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.FriendsAndRequestsLayout
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
        adapter = this@FriendsScreen.adapter
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
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutNoData)
        invisible()
        behavior(viewUnderHeaderBehavior)
        paddingHorizontal(40.dp)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          tag(TitleNoData)
          gravity(CENTER)
          textSize(TextSizes.H1)
          text(R.string.text_no_friends)
        }
        TextView(MatchParent, WrapContent, style = BaseTextView) {
          tag(TextNoData)
          gravity(CENTER)
          textSize(TextSizes.H4)
          margins(top = 32.dp)
          text(R.string.text_add_people_to_friends)
        }
        TextView(WrapContent, WrapContent, style = ClickableButton()) {
          text(R.string.text_find_people)
          margins(top = 32.dp)
          onClick { viewAs<PullToRefreshView>().hide() }
        }
      }
      child<FriendsAndRequestsLayout>(MatchParent, WrapContent) {
        classNameTag()
        invisible()
        layoutGravity(Gravity.BOTTOM)
        onClick { type -> onChipClicked(type) }
      }
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          if (view(LayoutNoData).isVisible) return@lb true
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
      FriendsRepository(
        FirebaseAuthenticator,
        FirebaseUserInfoDatabase(AndroidDispatchers),
        AndroidDispatchers
      ),
      AndroidDispatchers
    )
  }
  
  private val adapter = FriendsAdapter()
  
  override fun onInit() {
    presenter.loadListOf(ALL_FRIENDS)
  }
  
  override fun showLoading(type: FriendsType) {
    when (type) {
      ALL_FRIENDS -> {
        textView(TextLoading).text(R.string.text_loading_friends)
      }
      MY_REQUESTS -> {
        textView(TextLoading).text(R.string.text_loading_my_requests)
      }
      FRIENDS_REQUESTS -> {
        textView(TextLoading).text(R.string.text_loading_friend_requests)
    
      }
    }
    viewAs<FriendsAndRequestsLayout>().animateInvisible(duration = DURATION_SHORT)
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(view(LayoutLoading))
  }
  
  override fun showNoData(type: FriendsType) {
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    showLayout(view(LayoutNoData))
    showEmptyDataForType(type)
  }
  
  override fun showList(type: FriendsType, list: List<User>) {
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    showLayout(viewAs<RecyclerView>())
    if (adapter.currentFriendsType != type) {
      adapter.changeList(list)
    } else {
      adapter.submitList(list)
    }
    adapter.currentFriendsType = type
  }
  
  override fun showSwitchedToList(type: FriendsType, list: List<User>) {
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
  }
  
  override fun showSwitchedToEmptyView(type: FriendsType) {
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(view(LayoutNoData))
    showEmptyDataForType(type)
  }
  
  override fun showFailure(e: Throwable) {
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    showLayout(view(LayoutFailure))
  }
  
  private fun showLayout(layout: View) {
    layout.animateVisible()
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutNoData).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutFailure).ifTrue({ it !== layout }, { animateInvisible() })
    viewAs<PullToRefreshView>().hide()
  }
  
  private fun showEmptyDataForType(type: FriendsType) {
    viewAs<PullToRefreshView>().hide()
    view(LayoutLoading).animateInvisible()
    view(LayoutNoData).animateVisible()
    when (type) {
      ALL_FRIENDS -> {
        textView(TitleNoData).text(R.string.text_no_friends)
        textView(TextNoData).visible()
      }
      MY_REQUESTS -> {
        textView(TitleNoData).text(R.string.text_no_my_requests)
        textView(TextNoData).gone()
      }
      FRIENDS_REQUESTS -> {
        textView(TitleNoData).text(R.string.text_no_friend_requests)
        textView(TextNoData).gone()
      }
    }
  }
  
  private fun onChipClicked(type: FriendsType) {
    presenter.loadListOf(type)
    viewAs<Toolbar>().title(
      when (type) {
        ALL_FRIENDS -> R.string.title_friends
        MY_REQUESTS -> R.string.text_my_requests
        FRIENDS_REQUESTS -> R.string.text_friend_requests
      }
    )
  }
  
  companion object {
    
    const val TextLoading = "TextLoading"
    const val TitleNoData = "TitleNoData"
    const val TextNoData = "TextNoData"
    const val LayoutLoading = "LayoutLoading"
    const val LayoutFailure = "LayoutFailure"
    const val LayoutNoData = "LayoutNoData"
  }
}