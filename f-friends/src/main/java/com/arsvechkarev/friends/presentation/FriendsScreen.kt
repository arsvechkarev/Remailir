package com.arsvechkarev.friends.presentation

import android.text.Spannable
import android.text.SpannableString
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.FriendsComponent
import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsPagerScreen
import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMePagerScreen
import viewdsl.Ints.dp
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.drawables
import viewdsl.gone
import viewdsl.invisible
import viewdsl.onClick
import viewdsl.text
import viewdsl.visible
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Toolbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import core.model.FriendsType
import core.model.User
import core.resources.Colors
import core.resources.Dimens
import core.resources.Styles
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import core.ui.CustomFontSpan
import core.ui.navigation.Screen
import core.ui.navigation.ViewPagerAdapter
import core.ui.utils.moxyPresenter
import viewdsl.Size
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.Size.IntSize
import viewdsl.backgroundColor
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.constraints
import viewdsl.drawablePadding
import viewdsl.gravity
import viewdsl.id
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.textColor
import viewdsl.textSize

class FriendsScreen : Screen(), FriendsView {
  
  override fun buildLayout() = withViewBuilder {
    RootConstraintLayout {
      id(ConstraintLayoutId)
      child<Toolbar>(MatchParent, WrapContent) {
        id(ToolbarId)
        title("Friends")
        constraints { topToTopOf(parent) }
      }
      child<TabLayout>(MatchParent, WrapContent) {
        id(TabLayoutId)
        backgroundColor(core.resources.Colors.Toolbar)
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
        adapter = ViewPagerAdapter(
          listOf(AllFriendsPagerScreen(), MyRequestsPagerScreen(), RequestsToMePagerScreen())
        )
      }
      child<SimpleDialog>(MatchParent, MatchParent) {
        classNameTag()
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
            backgroundRoundRect(core.resources.Dimens.DefaultCornerRadius,
              Colors.Dialog)
            TextView(WrapContent, WrapContent, style = core.resources.Styles.BaseTextView) {
              id(TextConfirmation)
              gravity(Gravity.CENTER)
              textSize(TextSizes.H4)
              textColor(core.resources.Colors.TextPrimary)
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
                apply(ClickableTextView(core.resources.Colors.ErrorRipple,
                  Colors.Dialog))
                id(TextProceed)
                margins(start = 12.dp)
                text(R.string.text_remove_all_caps)
                textSize(TextSizes.H5)
                textColor(core.resources.Colors.Error)
              }
            }
          }
        }
      }
    }
  }
  
  private val presenter by moxyPresenter { FriendsComponent.get().provideFriendsPresenter() }
  
  override fun onInit() {
    TabLayoutMediator(viewAs(TabLayoutId), viewAs(ViewPagerId)) { tab, position ->
      val text = when (position) {
        0 -> R.string.text_all_friends
        1 -> R.string.text_my_requests
        2 -> R.string.text_friend_requests
        else -> throw IllegalStateException()
      }
      tab.text = getString(text)
    }.attach()
  }
  
  override fun showOnUserClicked(friendsType: FriendsType, user: User) {
    viewAs<SimpleDialog>().show()
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
        acceptText.drawables(start = R.drawable.ic_message,
          color = core.resources.Colors.TextPrimary)
        acceptText.onClick { navigator.startChatWith(user) }
        dismissText.text(R.string.text_remove_from_friends)
        dismissText.onClick { presenter.askForFriendRemovingConfirmation(user) }
        dismissText.drawables(start = R.drawable.ic_remove_firend,
          color = core.resources.Colors.TextPrimary)
      }
      FriendsType.MY_REQUESTS -> {
        acceptText.gone()
        dismissText.text(R.string.text_cancel_request)
        dismissText.onClick { presenter.sendCancelMyRequestAction(user) }
        dismissText.drawables(start = R.drawable.ic_cancel_circle,
          color = core.resources.Colors.TextPrimary)
      }
      FriendsType.REQUESTS_TO_ME -> {
        acceptText.visible()
        acceptText.text(R.string.text_accept_request)
        acceptText.drawables(start = R.drawable.ic_add_friend,
          color = core.resources.Colors.TextPrimary)
        acceptText.onClick { presenter.sendAcceptRequestAction(user) }
        dismissText.text(R.string.text_dismiss_request)
        dismissText.onClick { presenter.sendDismissRequestAction(user) }
        dismissText.drawables(start = R.drawable.ic_dismiss_circle,
          color = core.resources.Colors.TextPrimary)
      }
    }
  }
  
  override fun showRemovingFriendConfirmationDialog(user: User) {
    textView(TextConfirmation).applyConfirmationSpan(user.username,
      R.string.text_confirm_remove_from_friends,
      R.string.text_confirm_remove_from_friends_first_part
    )
    view(DialogDefault).animateInvisible()
    view(DialogConfirmation).animateVisible()
    view(TextProceed).onClick { presenter.sendRemoveFriendAction(user) }
    view(TextDismiss).onClick { viewAs<SimpleDialog>().hide() }
  }
  
  override fun hideDialog() {
    viewAs<SimpleDialog>().hide()
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
    setText(spannable)
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