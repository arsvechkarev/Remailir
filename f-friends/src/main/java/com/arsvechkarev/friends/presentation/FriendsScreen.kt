package com.arsvechkarev.friends.presentation

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMePagerScreen
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import core.model.FriendsType
import core.model.User
import core.resources.Colors
import core.resources.Dimens
import core.resources.Fonts
import core.resources.Styles
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import core.ui.ViewPagerAdapter
import core.ui.spans.spannable
import core.ui.utils.moxyPresenter
import core.ui.utils.shouldAnimate
import navigation.BaseScreen
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.backgroundColor
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.drawablePadding
import viewdsl.drawables
import viewdsl.gone
import viewdsl.gravity
import viewdsl.id
import viewdsl.invisible
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.visible
import viewdsl.withViewBuilder

class FriendsScreen : BaseScreen(), FriendsView {
  
  override fun buildLayout(context: Context) = context.withViewBuilder {
    RootConstraintLayout {
      id(ConstraintLayoutId)
      child<Toolbar>(MatchParent, WrapContent) {
        id(ToolbarId)
        title("Friends")
        constraints { topToTopOf(parent) }
      }
      child<TabLayout>(MatchParent, WrapContent) {
        id(TabLayoutId)
        backgroundColor(Colors.Toolbar)
        constraints { topToBottomOf(ToolbarId) }
      }
      child<ViewPager2>(MatchParent, IntSize(0)) {
        id(ViewPagerId)
        constraints {
          topToBottomOf(TabLayoutId)
          bottomToBottomOf(parent)
          startToStartOf(parent)
          endToEndOf(parent)
        }
        adapter = this@FriendsScreen.adapter
      }
      child<SimpleDialog>(MatchParent, MatchParent) {
        classNameTag()
        onHide = { presenter.onHideDialog() }
        FrameLayout(WrapContent, WrapContent) {
          layoutGravity(Gravity.CENTER)
          VerticalLayout(WrapContent, WrapContent) {
            id(DialogDefault)
            paddingVertical(12.dp)
            paddingHorizontal(20.dp)
            marginHorizontal(32.dp)
            gravity(Gravity.CENTER)
            layoutGravity(Gravity.CENTER)
            backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
            TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
              id(TextNameOfOtherUser)
              textSize(TextSizes.H1)
              textColor(Colors.TextSecondary)
              margins(bottom = 12.dp)
            }
            TextView(WrapContent, WrapContent) {
              apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
              id(TextAcceptRequest)
              margins(top = 12.dp)
              drawablePadding(16.dp)
            }
            TextView(WrapContent, WrapContent) {
              apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
              id(TextDismissOrRemove)
              drawablePadding(16.dp)
              margins(top = 12.dp)
            }
          }
          VerticalLayout(WrapContent, WrapContent) {
            id(DialogConfirmation)
            paddingVertical(12.dp)
            paddingHorizontal(20.dp)
            marginHorizontal(32.dp)
            gravity(Gravity.CENTER)
            layoutGravity(Gravity.CENTER)
            backgroundRoundRect(Dimens.DefaultCornerRadius,
              Colors.Dialog)
            TextView(WrapContent, WrapContent, style = Styles.BaseTextView) {
              id(TextConfirmation)
              gravity(Gravity.CENTER)
              textSize(TextSizes.H4)
              textColor(Colors.TextPrimary)
              margins(top = 12.dp, bottom = 24.dp)
            }
            HorizontalLayout(WrapContent, WrapContent) {
              TextView(WrapContent, WrapContent) {
                apply(ClickableTextView(Colors.Ripple, Colors.Dialog))
                id(TextDismiss)
                margins(end = 12.dp)
                text(R.string.text_cancel_all_caps)
                textSize(TextSizes.H5)
              }
              TextView(WrapContent, WrapContent) {
                apply(ClickableTextView(Colors.ErrorRipple, Colors.Dialog))
                id(TextProceed)
                margins(start = 12.dp)
                text(R.string.text_remove_all_caps)
                textSize(TextSizes.H5)
                textColor(Colors.Error)
              }
            }
          }
        }
      }
    }
  }
  
  private val adapter = ViewPagerAdapter(
    mvpDelegate,
    listOf(AllFriendsPagerScreen(), MyRequestsPagerScreen(), RequestsToMePagerScreen())
  )
  
  private val presenter by moxyPresenter { FriendsComponent.get().provideFriendsPresenter() }
  
  override fun onInit() {
    TabLayoutMediator(viewAs(TabLayoutId), viewAs(ViewPagerId)) { tab, position ->
      val text = when (position) {
        0 -> R.string.text_all_friends
        1 -> R.string.text_my_requests
        2 -> R.string.text_friend_requests
        else -> throw IllegalStateException()
      }
      tab.text = getText(text)
    }.attach()
  }
  
  override fun showActionDialog(friendsType: FriendsType, user: User) {
    viewAs<SimpleDialog>().show(animate = shouldAnimate(presenter))
    view(DialogConfirmation).invisible()
    view(DialogDefault).visible()
    view(DialogDefault).alpha = 1f
    textView(TextNameOfOtherUser).text(user.username)
    val dismissText = textView(TextDismissOrRemove)
    val acceptText = textView(TextAcceptRequest)
    when (friendsType) {
      FriendsType.ALL_FRIENDS -> {
        acceptText.visible()
        acceptText.text(R.string.text_start_chatting)
        acceptText.drawables(start = R.drawable.ic_message, color = Colors.TextPrimary)
        acceptText.onClick { presenter.startChatWith(user) }
        dismissText.text(R.string.text_remove_from_friends)
        dismissText.onClick { presenter.askForFriendRemovingConfirmation(user) }
        dismissText.drawables(start = R.drawable.ic_remove_firend,
          color = Colors.TextPrimary)
      }
      FriendsType.MY_REQUESTS -> {
        acceptText.gone()
        dismissText.text(R.string.text_cancel_request)
        dismissText.onClick { presenter.sendCancelMyRequestAction(user) }
        dismissText.drawables(start = R.drawable.ic_cancel_circle,
          color = Colors.TextPrimary)
      }
      FriendsType.REQUESTS_TO_ME -> {
        acceptText.visible()
        acceptText.text(R.string.text_accept_request)
        acceptText.drawables(start = R.drawable.ic_add_friend,
          color = Colors.TextPrimary)
        acceptText.onClick { presenter.sendAcceptRequestAction(user) }
        dismissText.text(R.string.text_dismiss_request)
        dismissText.onClick { presenter.sendDismissRequestAction(user) }
        dismissText.drawables(start = R.drawable.ic_dismiss_circle,
          color = Colors.TextPrimary)
      }
    }
  }
  
  override fun hideActionDialog() {
    viewAs<SimpleDialog>().hide(animate = shouldAnimate(presenter))
  }
  
  override fun showRemovingFriendConfirmationDialog(user: User) {
    val text = spannable {
      +getText(R.string.text_confirm_remove_from_friends_first_part)
      +user.username.withFont(Fonts.SegoeUiBold)
      +getText(R.string.text_confirm_remove_from_friends_second_part)
    }
    textView(TextConfirmation).text(text)
    view(DialogDefault).animateInvisible()
    view(DialogConfirmation).animateVisible()
    view(TextProceed).onClick { presenter.sendRemoveFriendAction(user) }
    view(TextDismiss).onClick { presenter.onHideDialog() }
  }
  
  override fun hideRemovingFriendConfirmationDialog() {
    view(DialogDefault).animateVisible()
    view(DialogConfirmation).animateInvisible()
  }
  
  override fun onRemovingFromScreen() {
    adapter.releaseScreens()
  }
  
  override fun onRelease() {
    FriendsComponent.clear()
  }
  
  override fun handleBackPress(): Boolean {
    presenter.handleBackPress()
    return true
  }
  
  companion object {
    
    private val ConstraintLayoutId = View.generateViewId()
    private val ToolbarId = View.generateViewId()
    private val TabLayoutId = View.generateViewId()
    private val ViewPagerId = View.generateViewId()
    private val DialogDefault = View.generateViewId()
    private val DialogConfirmation = View.generateViewId()
    private val TextConfirmation = View.generateViewId()
    private val TextDismiss = View.generateViewId()
    private val TextProceed = View.generateViewId()
    private val TextNameOfOtherUser = View.generateViewId()
    private val TextAcceptRequest = View.generateViewId()
    private val TextDismissOrRemove = View.generateViewId()
  }
}