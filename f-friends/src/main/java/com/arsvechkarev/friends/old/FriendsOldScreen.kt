package com.arsvechkarev.friends.old

import android.text.Spannable
import android.text.SpannableString
import android.view.Gravity.BOTTOM
import android.view.Gravity.CENTER
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arsvechkarev.views.ErrorLayout
import com.arsvechkarev.views.ImageError
import com.arsvechkarev.views.LayoutError
import com.arsvechkarev.views.TextError
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.R.drawable.ic_add_friend
import com.arsvechkarev.friends.R.drawable.ic_cancel_circle
import com.arsvechkarev.friends.R.drawable.ic_dismiss_circle
import com.arsvechkarev.friends.R.drawable.ic_message
import com.arsvechkarev.friends.R.drawable.ic_remove_firend
import com.arsvechkarev.friends.R.string.text_confirm_remove_from_friends
import com.arsvechkarev.friends.R.string.text_confirm_remove_from_friends_first_part
import viewdsl.DURATION_SHORT
import viewdsl.Ints.dp
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.drawables
import viewdsl.gone
import viewdsl.invisible
import viewdsl.onClick
import viewdsl.text
import viewdsl.visible
import com.arsvechkarev.views.ComplexProgressBar
import com.arsvechkarev.views.FriendsAndRequestsLayout
import com.arsvechkarev.views.PullToRefreshView
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Snackbar
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.behaviors.HeaderBehavior
import com.arsvechkarev.views.behaviors.PullToRefreshBehavior
import com.arsvechkarev.views.behaviors.ScrollingRecyclerBehavior
import com.arsvechkarev.views.behaviors.ViewUnderHeaderBehavior
import core.model.FriendsType
import core.model.FriendsType.ALL_FRIENDS
import core.model.FriendsType.MY_REQUESTS
import core.model.FriendsType.REQUESTS_TO_ME
import core.model.User
import core.model.UserAction
import core.model.UserAction.ADDING_TO_FRIENDS
import core.model.UserAction.CANCELING_MY_REQUEST
import core.model.UserAction.DISMISSING_REQUEST
import core.model.UserAction.REMOVE_FROM_FRIENDS
import core.resources.Colors
import core.resources.Colors.TextPrimary
import core.resources.Dimens
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles.BaseTextView
import core.resources.Styles.BoldTextView
import core.resources.Styles.ClickableButton
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import core.ui.CustomFontSpan
import core.ui.navigation.Screen
import core.ui.utils.getMessageRes
import core.ui.utils.ifTrue
import timber.log.Timber
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundRoundRect
import viewdsl.behavior
import viewdsl.classNameTag
import viewdsl.drawablePadding
import viewdsl.gravity
import viewdsl.isVisible
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.paddings
import viewdsl.tag
import viewdsl.textColor
import viewdsl.textSize

class FriendsOldScreen : Screen(), FriendsOldView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      val viewUnderHeaderBehavior = ViewUnderHeaderBehavior()
      val headerBehavior = HeaderBehavior()
      child<Toolbar>(MatchParent, WrapContent) {
        classNameTag()
        showSearchImage = true
        title(R.string.title_friends)
        onBackClick { navigator.popCurrentScreen() }
        //        onSearchTyped { text -> presenter.performFiltering(text) }
        //        onExitFromSearchMode = { presenter.showCurrentListIfNotEmpty() }
        behavior(headerBehavior)
      }
      child<RecyclerView>(MatchParent, MatchParent) {
        classNameTag()
        invisible()
        paddings(top = 16.dp)
        behavior(ScrollingRecyclerBehavior())
        layoutManager = LinearLayoutManager(context)
        adapter = this@FriendsOldScreen.adapter
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
      ErrorLayout()
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
          onClick { navigator.goToSearchScreen() }
        }
      }
      child<FriendsAndRequestsLayout>(MatchParent, WrapContent) {
        classNameTag()
        invisible()
        layoutGravity(BOTTOM)
        onClick { type -> onChipClicked(type) }
      }
      child<PullToRefreshView>(MatchParent, MatchParent) {
        classNameTag()
        val behavior = PullToRefreshBehavior(context)
        behavior(behavior)
        //        onRefreshPulled = { presenter.onRefreshPulled() }
        behavior.allowPulling = lb@{
          if (view(LayoutNoData).isVisible) return@lb true
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
        FrameLayout(WrapContent, WrapContent) {
          layoutGravity(CENTER)
          VerticalLayout(WrapContent, WrapContent) {
            tag(DialogDefault)
            paddingVertical(12.dp)
            paddingHorizontal(20.dp)
            marginHorizontal(32.dp)
            gravity(CENTER)
            layoutGravity(CENTER)
            backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
            TextView(WrapContent, WrapContent, style = BoldTextView) {
              tag(TextNameOfOtherUser)
              textSize(TextSizes.H1)
              textColor(Colors.TextSecondary)
              margins(bottom = 12.dp)
            }
            TextView(WrapContent, WrapContent) {
              apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
              tag(TextAcceptRequest)
              margins(top = 12.dp)
              drawablePadding(16.dp)
            }
            TextView(WrapContent, WrapContent) {
              apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
              tag(TextDismissOrRemove)
              drawablePadding(16.dp)
              margins(top = 12.dp)
            }
          }
          VerticalLayout(WrapContent, WrapContent) {
            tag(DialogConfirmation)
            paddingVertical(12.dp)
            paddingHorizontal(20.dp)
            marginHorizontal(32.dp)
            gravity(CENTER)
            layoutGravity(CENTER)
            backgroundRoundRect(Dimens.DefaultCornerRadius,
              Colors.Dialog)
            TextView(WrapContent, WrapContent, style = BaseTextView) {
              tag(TextConfirmation)
              gravity(CENTER)
              textSize(TextSizes.H4)
              textColor(TextPrimary)
              margins(top = 12.dp, bottom = 24.dp)
            }
            HorizontalLayout(WrapContent, WrapContent) {
              TextView(WrapContent, WrapContent) {
                apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
                tag(TextDismiss)
                margins(end = 12.dp)
                text(R.string.text_cancel_all_caps)
                textSize(TextSizes.H5)
              }
              TextView(WrapContent, WrapContent) {
                apply(ClickableTextView(Colors.ErrorRipple,
                  Colors.Dialog))
                tag(TextProceed)
                margins(start = 12.dp)
                text(R.string.text_remove_all_caps)
                textSize(TextSizes.H5)
                textColor(Colors.Error)
              }
            }
          }
        }
      }
      child<Snackbar>(MatchParent, WrapContent) {
        classNameTag()
        layoutGravity(BOTTOM)
        margins(start = 12.dp, end = 12.dp, bottom = 12.dp)
      }
    }
  }
  
  //  private val presenter by moxyPresenter {
  //    FriendsOldPresenter(
  //      FriendsOldInteractor(
  //        FriendsRepositoryImpl(
  //          User(core.impl.firebase.FirebaseAuthenticator.getUsername()),
  //          core.impl.firebase.PathDatabaseSchema,
  //          core.impl.firebase.FirebaseDatabaseImpl(core.impl.AndroidDispatchers),
  //          ByUsernameUsersActions,
  //          UserMapper
  //        )
  //      ),
  //      core.impl.AndroidDispatchers
  //    )
  //  }
  
  private val adapter = FriendsOldAdapter(onUserClicked = { user ->
    //    presenter.onUserClicked(user)
  })
  
  override fun onAppearedOnScreen() {
    //    presenter.loadList()
  }
  
  override fun onOrientationBecamePortrait() {
    view(ImageError).visible()
  }
  
  override fun onOrientationBecameLandscape() {
    view(ImageError).gone()
  }
  
  override fun showLoading(type: FriendsType) {
    viewAs<Toolbar>().animateSearchInvisible()
    textView(TextLoading).text(when (type) {
      ALL_FRIENDS -> R.string.text_loading_friends
      MY_REQUESTS -> R.string.text_loading_my_requests
      REQUESTS_TO_ME -> R.string.text_loading_friend_requests
    })
    viewAs<FriendsAndRequestsLayout>().animateInvisible(duration = DURATION_SHORT)
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(view(LayoutLoading))
  }
  
  override fun showNoData(type: FriendsType) {
    viewAs<Toolbar>().animateSearchInvisible()
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    showLayout(view(LayoutNoData))
    showEmptyDataForType(type)
  }
  
  override fun showList(type: FriendsType, list: List<User>) {
    viewAs<Toolbar>().animateSearchVisible()
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    showLayout(viewAs<RecyclerView>())
    if (adapter.currentFriendsType != type) {
      //      adapter.changeListWithoutAnimation(list)
    } else {
      adapter.submitList(list)
    }
    adapter.currentFriendsType = type
  }
  
  override fun showSearchResult(list: List<User>) {
    adapter.submitList(list)
  }
  
  override fun showUserDialog(friendsType: FriendsType, user: User) {
    viewAs<SimpleDialog>().show()
    view(DialogConfirmation).invisible()
    view(DialogDefault).visible()
    view(DialogDefault).alpha = 1f
    textView(TextNameOfOtherUser).text(user.username)
    val dismissText = textView(TextDismissOrRemove)
    val acceptText = textView(TextAcceptRequest)
    when (friendsType) {
      ALL_FRIENDS -> {
        acceptText.visible()
        acceptText.text(R.string.text_start_chatting)
        acceptText.drawables(start = ic_message, color = TextPrimary)
        acceptText.onClick { navigator.startChatWith(user) }
        dismissText.text(R.string.text_remove_from_friends)
        //        dismissText.onClick { presenter.askForFriendRemovingConfirmation(user) }
        dismissText.drawables(start = ic_remove_firend, color = TextPrimary)
      }
      MY_REQUESTS -> {
        acceptText.gone()
        dismissText.text(R.string.text_cancel_request)
        //        dismissText.onClick { presenter.performAction(CANCELING_MY_REQUEST, user) }
        dismissText.drawables(start = ic_cancel_circle, color = TextPrimary)
      }
      REQUESTS_TO_ME -> {
        acceptText.visible()
        acceptText.text(R.string.text_accept_request)
        acceptText.drawables(start = ic_add_friend, color = TextPrimary)
        //        acceptText.onClick { presenter.performAction(ADDING_TO_FRIENDS, user) }
        dismissText.text(R.string.text_dismiss_request)
        //        dismissText.onClick { presenter.performAction(DISMISSING_REQUEST, user) }
        dismissText.drawables(start = ic_dismiss_circle, color = TextPrimary)
      }
    }
  }
  
  override fun showRemovingFriendConfirmationDialog(user: User) {
    textView(TextConfirmation).applyConfirmationSpan(user.username,
      text_confirm_remove_from_friends, text_confirm_remove_from_friends_first_part
    )
    view(DialogDefault).animateInvisible()
    view(DialogConfirmation).animateVisible()
    //    view(TextProceed).onClick { presenter.performAction(REMOVE_FROM_FRIENDS, user) }
    view(TextDismiss).onClick { viewAs<SimpleDialog>().hide() }
  }
  
  override fun showSwitchedToList(type: FriendsType, list: List<User>) {
    viewAs<Toolbar>().animateSearchVisible()
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
    adapter.currentFriendsType = type
  }
  
  override fun showSwitchedToEmptyView(type: FriendsType) {
    viewAs<Toolbar>().animateSearchInvisible()
    viewAs<FriendsAndRequestsLayout>().close()
    showLayout(view(LayoutNoData))
    showEmptyDataForType(type)
  }
  
  override fun showFailure(e: Throwable) {
    Timber.d(e)
    viewAs<Toolbar>().animateSearchInvisible()
    viewAs<FriendsAndRequestsLayout>().animateVisible(duration = DURATION_SHORT)
    textView(TextError).text(e.getMessageRes())
    //    textView(TextRetry).onClick { presenter.loadList(allowUseCache = false) }
    showLayout(view(LayoutError))
  }
  
  override fun showLoadingUserAction(userAction: UserAction) {
    viewAs<Toolbar>().animateSearchInvisible()
    viewAs<SimpleDialog>().hide()
    val snackbar = viewAs<Snackbar>()
    snackbar.show()
    snackbar.switchToLoadingMode()
    when (userAction) {
      REMOVE_FROM_FRIENDS -> snackbar.textLoading.text(R.string.text_removing_from_friends)
      CANCELING_MY_REQUEST -> snackbar.textLoading.text(R.string.text_cancelling_request)
      ADDING_TO_FRIENDS -> snackbar.textLoading.text(R.string.text_accepting_request)
      DISMISSING_REQUEST -> snackbar.textLoading.text(R.string.text_dismissing_request)
    }
  }
  
  override fun showCompletedUserAction(type: FriendsType) {
    viewAs<Snackbar>().hide()
    showLayout(view(LayoutNoData))
    showEmptyDataForType(type)
  }
  
  override fun showCompletedUserAction(type: FriendsType, list: List<User>) {
    viewAs<Toolbar>().animateSearchVisible()
    viewAs<Snackbar>().hide()
    showLayout(viewAs<RecyclerView>())
    adapter.submitList(list)
    adapter.currentFriendsType = type
  }
  
  override fun showUserActionFailure(userAction: UserAction, user: User, e: Throwable) {
    viewAs<Toolbar>().animateSearchInvisible()
    Timber.d(e, "Friends error")
    val snackbar = viewAs<Snackbar>()
    snackbar.switchToErrorMode()
    snackbar.textError.text(R.string.error_unknown_short)
    //    snackbar.onRetryClicked { presenter.performAction(userAction, user) }
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
    view(LayoutNoData).ifTrue({ it !== layout }, { animateInvisible() })
    view(LayoutError).ifTrue({ it !== layout }, { animateInvisible() })
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
      REQUESTS_TO_ME -> {
        textView(TitleNoData).text(R.string.text_no_friend_requests)
        textView(TextNoData).gone()
      }
    }
  }
  
  private fun onChipClicked(type: FriendsType) {
    //    presenter.loadList(type)
    viewAs<Toolbar>().title(
      when (type) {
        ALL_FRIENDS -> R.string.title_friends
        MY_REQUESTS -> R.string.text_my_requests
        REQUESTS_TO_ME -> R.string.text_friend_requests
      }
    )
  }
  
  private fun TextView.applyConfirmationSpan(
    username: String,
    textRes: Int,
    firstPartOfTheText: Int,
  ) {
    val firstPartOfText = getString(firstPartOfTheText)
    val text = getString(textRes, username)
    val spannable = SpannableString(text)
    val indexOfText = text.indexOf(username, startIndex = firstPartOfText.length)
    spannable.setSpan(
      CustomFontSpan(core.resources.Fonts.SegoeUiBold),
      indexOfText, indexOfText + username.length,
      Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text(spannable)
  }
  
  companion object {
    
    private const val DialogDefault = "DialogDefault"
    private const val DialogConfirmation = "DialogConfirmation"
    private const val TextLoading = "TextLoading"
    private const val TextConfirmation = "TextConfirmation"
    private const val TextDismiss = "TextDismiss"
    private const val TextProceed = "TextProceed"
    private const val TextNameOfOtherUser = "TextNameOfOtherUser"
    private const val TextAcceptRequest = "TextAcceptRequest"
    private const val TextDismissOrRemove = "TextDismissOrRemove"
    private const val TitleNoData = "TitleNoData"
    private const val TextNoData = "TextNoData"
    private const val LayoutLoading = "LayoutLoading"
    private const val LayoutNoData = "LayoutNoData"
  }
}