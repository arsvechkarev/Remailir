package com.arsvechkarev.search.presentation

import android.view.Gravity
import android.view.Gravity.CENTER
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.common.ErrorLayout
import com.arsvechkarev.common.ImageError
import com.arsvechkarev.common.LayoutError
import com.arsvechkarev.common.TextError
import com.arsvechkarev.common.TextRetry
import com.arsvechkarev.core.WAITING_TIME
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.getMessageRes
import com.arsvechkarev.core.extenstions.ifTrue
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.database.FirebaseDatabaseImpl
import com.arsvechkarev.firebase.database.PathDatabaseSchema
import com.arsvechkarev.search.R
import com.arsvechkarev.search.domain.RequestResult
import com.arsvechkarev.search.domain.RequestResult.ERROR_ALREADY_FRIENDS
import com.arsvechkarev.search.domain.RequestResult.ERROR_REQUEST_ALREADY_SENT
import com.arsvechkarev.search.domain.RequestResult.ERROR_THIS_USER_ALREADY_HAS_REQUEST
import com.arsvechkarev.search.domain.SearchRepository
import com.arsvechkarev.search.list.SearchAdapter
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.backgroundRoundRect
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
import com.arsvechkarev.viewdsl.paddingVertical
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.visible
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Snackbar
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.behaviors.HeaderBehavior
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import com.arsvechkarev.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import timber.log.Timber

class SearchScreen : Screen(), SearchView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
      val headerBehavior = HeaderBehavior()
      child<Toolbar>(MatchParent, WrapContent) {
        behavior(headerBehavior)
        classNameTag()
        showSearchImage = true
        title(R.string.title_people)
        onBackClick { navigator.popCurrentScreen() }
        onSearchTyped { text -> presenter.performFiltering(text) }
        onExitFromSearchMode = { presenter.showCurrentList() }
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
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        onRefreshPulled = { presenter.onRefreshPulled() }
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
            textColor(Colors.Correct)
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
  
  private val presenter by moxyPresenter {
    SearchPresenter(
      SearchRepository(
        FirebaseAuthenticator.getUsername(),
        PathDatabaseSchema,
        FirebaseDatabaseImpl(AndroidDispatchers),
        AndroidDispatchers
      ),
      AndroidDispatchers
    )
  }
  
  private val adapter = SearchAdapter(onUserClicked = {
    textView(TextNameOfOtherUser).text(it.username)
    textView(TextSendRequest).onClick { presenter.sendFriendRequest(it.username) }
    viewAs<SimpleDialog>().show()
  })
  
  override fun onInit() {
    presenter.loadUsersList()
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
    textView(TextRetry).onClick { presenter.loadUsersList() }
    showLayout(view(LayoutError))
  }
  
  override fun showSendingRequest() {
    viewAs<SimpleDialog>().hide()
    viewAs<Snackbar>().switchToLoadingMode()
    viewAs<Snackbar>().textLoading.text(R.string.text_sending_request)
    viewAs<Snackbar>().show()
    viewNonNull.postDelayed({
      view?.findViewWithTag<Snackbar>(Snackbar::class.java.name)?.hide()
    }, WAITING_TIME)
  }
  
  override fun showRequestSent() {
    viewAs<Snackbar>().switchToInfoMode()
    viewAs<Snackbar>().textInfo.text(R.string.text_request_sent)
    viewNonNull.postDelayed({
      view?.findViewWithTag<Snackbar>(Snackbar::class.java.name)?.hide()
    }, WAITING_TIME)
  }
  
  override fun showSendingRequestFailure(result: RequestResult) {
    val text = when (result) {
      ERROR_ALREADY_FRIENDS -> R.string.error_user_already_friend
      ERROR_REQUEST_ALREADY_SENT -> R.string.error_request_already_sent
      ERROR_THIS_USER_ALREADY_HAS_REQUEST -> R.string.error_user_sent_request
      else -> throw IllegalStateException()
    }
    viewAs<Snackbar>().switchToErrorMode()
    viewAs<Snackbar>().textError.text(text)
    viewAs<Snackbar>().buttonRetry.invisible()
  }
  
  override fun showSendingRequestFailure(e: Throwable, username: String) {
    viewAs<Snackbar>().switchToErrorMode()
    viewAs<Snackbar>().buttonRetry.onClick { presenter.sendFriendRequest(username) }
    viewAs<Snackbar>().textError.text(R.string.error_unknown_short)
  }
  
  override fun onBackPressed(): Boolean {
    val toolbar = viewAs<Toolbar>()
    if (toolbar.isInSearchMode) {
      toolbar.switchFromSearchMode()
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
    
    const val TextLoading = "TextLoading"
    const val TextNameOfOtherUser = "TextNameOfOtherUser"
    const val TextSendRequest = "TextSendRequest"
    const val LayoutLoading = "LayoutLoading"
  }
}