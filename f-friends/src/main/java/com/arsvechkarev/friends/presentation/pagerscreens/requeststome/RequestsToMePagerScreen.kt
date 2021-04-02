package com.arsvechkarev.friends.presentation.pagerscreens.requeststome

import android.content.Context
import android.view.Gravity.CENTER
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.CommonFriendsAdapter
import com.arsvechkarev.views.ComplexProgressBar
import core.model.User
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles.BaseTextView
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

class RequestsToMePagerScreen : ViewPagerScreen(), RequestsToMeView {
  
  override fun buildView(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      child<RecyclerView>(MatchParent, MatchParent) {
        id(RecyclerViewId)
        invisible()
        paddings(top = 16.dp)
        layoutManager = LinearLayoutManager(context)
        adapter = this@RequestsToMePagerScreen.adapter
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
        TextView(MatchParent, WrapContent, style = BaseTextView) {
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
  
  override fun showFailureLoadingList(e: Throwable) {
    Toast.makeText(contextNonNull, "error", Toast.LENGTH_LONG).show()
  }
  
  override fun showLoadingAcceptingRequest(user: User) {
    Toast.makeText(contextNonNull, "accepting request $user", Toast.LENGTH_LONG).show()
  }
  
  override fun showSuccessAcceptingRequest(user: User) {
    adapter.submitList(adapter.currentList - user)
  }
  
  override fun showFailureAcceptingRequest(user: User) {
  }
  
  override fun showLoadingDismissingRequest(user: User) {
  }
  
  override fun showSuccessDismissingRequest(user: User) {
    adapter.submitList(adapter.currentList - user)
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
    private val TextLoadingId = View.generateViewId()
    private val LayoutNoDataId = View.generateViewId()
    private val TextNoDataId = View.generateViewId()
    private val TitleNoDataId = View.generateViewId()
  }
}