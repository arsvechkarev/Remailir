package com.arsvechkarev.search.presentation

import android.content.Context
import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.search.R
import com.arsvechkarev.search.domain.RequestResult
import com.arsvechkarev.search.domain.RequestResult.ERROR_ALREADY_FRIENDS
import com.arsvechkarev.search.domain.RequestResult.ERROR_REQUEST_ALREADY_SENT
import com.arsvechkarev.search.domain.RequestResult.ERROR_THIS_USER_ALREADY_HAS_REQUEST
import com.arsvechkarev.search.list.SearchAdapter
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.ErrorLayout
import com.arsvechkarev.views.ImageError
import com.arsvechkarev.views.LayoutError
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Snackbar
import com.arsvechkarev.views.TextError
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.behaviors.HeaderBehavior
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import com.arsvechkarev.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import core.model.User
import core.resources.Colors
import core.resources.Dimens
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles.BoldTextView
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import core.ui.utils.getMessageRes
import core.ui.utils.ifTrue
import navigation.BaseScreen
import timber.log.Timber
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundRoundRect
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.gone
import viewdsl.gravity
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.margins
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.paddings
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class SearchScreen : BaseScreen(), SearchView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootCoordinatorLayout {
      val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
      val headerBehavior = HeaderBehavior()
      child<Toolbar>(MatchParent, WrapContent) {
        behavior(headerBehavior)
        classNameTag()
        showSearchImage = true
        title(R.string.title_people)
        //        onBackClick { navigator.popCurrentScreen() }
        //        onSearchTyped { text -> presenter.performFiltering(text) }
        //        onExitFromSearchMode = { presenter.showCurrentList() }
      }
      child<RecyclerView>(MatchParent, MatchParent) {
        classNameTag()
        invisible()
        paddings(top = 16.dp)
        behavior(ScrollingRecyclerBehavior())
        layoutManager = LinearLayoutManager(context)
        adapter = this@SearchScreen.adapter
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
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        //        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          if (viewAs<SimpleDialog>().isOpened) return@lb false
          if (viewAs<Toolbar>().isInSearchMode) return@lb false
          if (view(LayoutError).isVisible) return@lb false
          if (viewAs<RecyclerView>().isVisible
              && headerBehavior.getTopAndBottomOffset() == 0) return@lb true
          return@lb false
        }
      }
      child<SimpleDialog>(MatchParent, MatchParent) {
        classNameTag()
        VerticalLayout(WrapContent, WrapContent) {
          paddingVertical(12.dp)
          paddingHorizontal(20.dp)
          gravity(CENTER)
          backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            tag(TextNameOfOtherUser)
            textSize(TextSizes.H3)
            margins(bottom = 12.dp)
          }
          TextView(WrapContent, WrapContent, style = ClickableTextView(
            Colors.CorrectRipple, Colors.Dialog
          )) {
            tag(TextSendRequest)
            margins(top = 12.dp)
            text(R.string.text_send_friend_request)
            textColor(core.resources.Colors.Correct)
          }
        }
      }
      child<Snackbar>(MatchParent, WrapContent) {
        classNameTag()
        layoutGravity(Gravity.BOTTOM)
        margins(start = 12.dp, end = 12.dp, bottom = 12.dp)
      }
    }
  }
  
  //  private val presenter by moxyPresenter {
  //    SearchPresenter(
  //      SearchRepository(
  //        core.impl.firebase.FirebaseAuthenticator.getUsername(),
  //        core.impl.firebase.PathDatabaseSchema,
  //        core.impl.firebase.FirebaseDatabaseImpl(core.impl.AndroidDispatchers),
  //        core.impl.AndroidDispatchers
  //      ),
  //      core.impl.AndroidDispatchers
  //    )
  //  }
  
  private val adapter = SearchAdapter(onUserClicked = { user ->
    textView(TextNameOfOtherUser).text(user.username)
    //    textView(TextSendRequest).onClick { presenter.sendFriendRequest(user) }
    viewAs<SimpleDialog>().show(animate = true)
  })
  
  override fun onInit() {
    //    presenter.loadUsersList()
  }
  
  override fun onOrientationBecamePortrait() {
    view(ImageError).visible()
  }
  
  override fun onOrientationBecameLandscape() {
    view(ImageError).gone()
  }
  
  override fun showLoading() {
    viewAs<Toolbar>().animateSearchInvisible()
    showLayout(view(LayoutLoading))
  }
  
  override fun showUsersList(list: List<User>) {
    viewAs<Toolbar>().animateSearchVisible()
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
  }
  
  override fun showSearchResults(list: List<User>) {
    adapter.submitList(list)
  }
  
  override fun showFailure(e: Throwable) {
    Timber.d(e)
    viewAs<Toolbar>().animateSearchInvisible()
    textView(TextError).text(e.getMessageRes())
    //    textView(TextRetry).onClick { presenter.loadUsersList() }
    showLayout(view(LayoutError))
  }
  
  override fun showSendingRequest() {
    viewAs<SimpleDialog>().hide(animate = true)
    //    viewAs<Snackbar>().switchToLoadingMode()
    //    viewAs<Snackbar>().textLoading.text(R.string.text_sending_request)
    //    viewAs<Snackbar>().show()
    //    viewNonNull.postDelayed({
    //      view?.findViewWithTag<Snackbar>(Snackbar::class.java.name)?.hide()
    //    }, WAITING_TIME)
  }
  
  override fun showRequestSent() {
    //    viewAs<Snackbar>().switchToInfoMode()
    //    viewAs<Snackbar>().textInfo.text(R.string.text_request_sent)
    //    viewNonNull.postDelayed({
    //      view?.findViewWithTag<Snackbar>(Snackbar::class.java.name)?.hide()
    //    }, WAITING_TIME)
  }
  
  override fun showSendingRequestFailure(result: RequestResult) {
    val text = when (result) {
      ERROR_ALREADY_FRIENDS -> R.string.error_user_already_friend
      ERROR_REQUEST_ALREADY_SENT -> R.string.error_request_already_sent
      ERROR_THIS_USER_ALREADY_HAS_REQUEST -> R.string.error_user_sent_request
      else -> throw IllegalStateException()
    }
    //    viewAs<Snackbar>().switchToFailureMode(true)
    //    viewAs<Snackbar>().textFailure.text(text)
    //    viewAs<Snackbar>().buttonRetry.invisible()
  }
  
  override fun showSendingRequestFailure(e: Throwable, user: User) {
    //    viewAs<Snackbar>().switchToFailureMode(true, getString(R.string.error_unknown_short))
    //    viewAs<Snackbar>().buttonRetry.onClick { presenter.sendFriendRequest(user) }
    //    viewAs<Snackbar>().textFailure.text(R.string.error_unknown_short)
  }
  
  //  override fun onBackPressed(): Boolean {
  //    val toolbar = viewAs<Toolbar>()
  //    if (toolbar.isInSearchMode) {
  //      toolbar.switchFromSearchMode()
  //      return true
  //    }
  //    return false
  //  }
  
  private fun showLayout(layout: View) {
    layout.animateVisible()
    viewAs<RecyclerView>().ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutLoading).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutError).ifTrue({ it !== layout }, { animateInvisible() })
    viewAs<PullToRefreshView>().hide()
  }
  
  companion object {
    
    const val TextLoading = "TextLoading"
    const val TextNameOfOtherUser = "TextNameOfOtherUser"
    const val TextSendRequest = "TextSendRequest"
    const val LayoutLoading = "LayoutLoading"
  }
}