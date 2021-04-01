package com.arsvechkarev.friends.presentation.pagerscreens.myrequests

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.ifTrue
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.navigation.ViewPagerScreen
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsDi
import com.arsvechkarev.friends.presentation.CommonFriendsAdapter
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.id
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.paddingHorizontal
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.visible
import com.arsvechkarev.viewdsl.withViewBuilder
import com.arsvechkarev.views.ComplexProgressBar

class MyRequestsPagerScreen : ViewPagerScreen(), MyRequestsView {
  
  override fun buildView(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      child<RecyclerView>(MatchParent, MatchParent) {
        id(RecyclerViewId)
        invisible()
        paddings(top = 16.dp)
        layoutManager = LinearLayoutManager(context)
        adapter = this@MyRequestsPagerScreen.adapter
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(LayoutLoadingId)
        invisible()
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          id(TextLoadingId)
          gravity(CENTER)
          textSize(TextSizes.H3)
        }
        child<ComplexProgressBar>(Dimens.ProgressBarSizeBig, Dimens.ProgressBarSizeBig) {
          margins(top = 40.dp)
        }
      }
      VerticalLayout(MatchParent, WrapContent) {
        id(LayoutNoDataId)
        invisible()
        paddingHorizontal(40.dp)
        gravity(CENTER)
        layoutGravity(CENTER)
        TextView(MatchParent, WrapContent, style = BoldTextView) {
          id(TitleNoDataId)
          gravity(CENTER)
          textSize(TextSizes.H1)
          text(R.string.text_no_friends)
        }
        TextView(MatchParent, WrapContent, style = Styles.BaseTextView) {
          id(TextNoDataId)
          gravity(CENTER)
          textSize(TextSizes.H4)
          margins(top = 32.dp)
          text(R.string.text_add_people_to_friends)
        }
        TextView(WrapContent, WrapContent, style = ClickableButton()) {
          text(R.string.text_find_people)
          margins(top = 32.dp)
        }
      }
    }
  }
  
  private val adapter = CommonFriendsAdapter { presenter.onUserClicked(it) }
  
  private val presenter by moxyPresenter {
    MyRequestsPresenter(FriendsDi.friendsInteractor, FriendsDi.friendsScreensBridge,
      AndroidDispatchers)
  }
  
  override fun onInit() {
    presenter.startLoadingMyRequests()
    presenter.startListeningToMyRequestsChanges()
  }
  
  override fun showLoadingList() {
    showOnly(view(LayoutLoadingId))
  }
  
  override fun showListIsEmpty() {
    showOnly(view(LayoutNoDataId))
  }
  
  override fun showLoadedList(data: List<User>) {
    showOnly(view(RecyclerViewId))
    adapter.data = data.toMutableList()
    adapter.notifyDataSetChanged()
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
    private val TextLoadingId = View.generateViewId()
    private val LayoutNoDataId = View.generateViewId()
    private val TextNoDataId = View.generateViewId()
    private val TitleNoDataId = View.generateViewId()
  }
}