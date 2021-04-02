package com.arsvechkarev.friends.presentation.pagerscreens.myrequests

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.CommonFriendsAdapter
import com.arsvechkarev.views.ComplexProgressBar
import core.model.User
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles
import core.resources.Styles.BoldTextView
import core.resources.Styles.ClickableButton
import core.resources.TextSizes
import core.ui.navigation.ViewPagerScreen
import core.ui.utils.ifTrue
import core.ui.utils.moxyPresenter
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.gravity
import viewdsl.id
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.paddingHorizontal
import viewdsl.paddings
import viewdsl.text
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

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
        child<ComplexProgressBar>(ProgressBarSizeBig, ProgressBarSizeBig) {
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
    FriendsComponent.get().provideMyRequestsScreenPresenter()
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
    private val TextLoadingId = View.generateViewId()
    private val LayoutNoDataId = View.generateViewId()
    private val TextNoDataId = View.generateViewId()
    private val TitleNoDataId = View.generateViewId()
  }
}