package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.Gravity
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.views.ChatLayout
import com.arsvechkarev.views.SimpleDialog
import core.model.messaging.MessageOtherUser
import core.model.messaging.MessageThisUser
import core.resources.Colors
import core.resources.Dimens
import core.resources.Styles
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import core.ui.navigation.Screen
import timber.log.Timber
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.backgroundRoundRect
import viewdsl.childView
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.layoutGravity
import viewdsl.marginHorizontal
import viewdsl.margins
import viewdsl.onClick
import viewdsl.paddingHorizontal
import viewdsl.paddingVertical
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize

class ChatScreen : Screen(), ChatView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      addView(
        ChatLayout(context).apply {
          tag("ChatMainLayout")
          size(MatchParent, MatchParent)
          imageSend.onClick {
            if (editText.text.toString().isNotBlank()) {
              //              presenter.sendMessage(editText.text.toString())
            }
          }
        }
      )
      TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
        tag("Text")
        textSize(TextSizes.H2)
        layoutGravity(Gravity.CENTER)
      }
      addView(
        SimpleDialog(context).apply {
          classNameTag()
          size(MatchParent, MatchParent)
          VerticalLayout(WrapContent, WrapContent) {
            paddingVertical(12.dp)
            paddingHorizontal(20.dp)
            marginHorizontal(32.dp)
            gravity(Gravity.CENTER)
            layoutGravity(Gravity.CENTER)
            backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
            TextView(WrapContent, WrapContent) {
              tag("DialogTitle")
              textSize(TextSizes.H5)
              text("Dofpsd jfosd ijfoajsd\n sdlfkajshdlhkfjh a fsd\n sdfpplk sdkjf?")
              textColor(Colors.TextPrimary)
            }
            HorizontalLayout(WrapContent, WrapContent) {
              margins(top = 20.dp)
              TextView(WrapContent, WrapContent) {
                apply(ClickableTextView(Colors.CorrectRipple, Colors.Dialog))
                tag("DialogYes")
                text("YES")
                margins(end = 16.dp)
                textSize(TextSizes.H4)
                textColor(core.resources.Colors.Correct)
              }
              TextView(WrapContent, WrapContent, style = core.resources.Styles.BoldTextView) {
                apply(ClickableTextView(core.resources.Colors.ErrorRipple,
                  Colors.Dialog))
                tag("DialogNo")
                text("NO")
                margins(start = 16.dp)
                textSize(TextSizes.H4)
                textColor(core.resources.Colors.Error)
              }
            }
          }
        }
      )
    }
  }
  
  //  private val presenter by moxyPresenter {
  //    ChatPresenter(
  //      ChatInteractor(
  //        contextNonNull,
  //        authentication.impl.FirebaseAuthenticator.getUsername(),
  //        DefaultMessageFactory,
  //        ChatFirebaseDataSource(
  //          authentication.impl.FirebaseAuthenticator.getUsername(),
  //          AndroidDispatchers,
  //        ),
  //        FirebaseMessagingDataSource(
  //          authentication.impl.FirebaseAuthenticator.getUsername(),
  //          arguments!!.getString(KEY_USERNAME)!!,
  //          AndroidDispatchers
  //        ),
  //      ),
  //      core.impl.AndroidDispatchers
  //    )
  //  }
  
  private val chatLayout get() = viewNonNull.childView("ChatMainLayout") as ChatLayout
  
  private val adapter = ChatAdapter()
  
  override fun onInit(arguments: Bundle) {
    val otherUserUsername = arguments.getString(KEY_USERNAME)!!
    val type = arguments.getString(KEY_TYPE)
    //    presenter.initialize(type!!, otherUserUsername)
    chatLayout.toolbar.title(otherUserUsername)
    chatLayout.recyclerView.adapter = adapter
  }
  
  override fun showThisUserWaitingForChat() {
    textView("Text").text("Waiting for joining")
  }
  
  override fun showThisUserJoined() {
    textView("Text").text("Joined for chat")
  }
  
  override fun showOtherUserJoined() {
    Timber.d("User joined")
    textView("Text").text("Joined")
  }
  
  override fun showMessageReceived(messageOtherUser: MessageOtherUser) {
    //    adapter.addItem(messageOtherUser)
  }
  
  override fun showMessageSent(messageThisUser: MessageThisUser) {
    //    adapter.addItem(messageThisUser)
    //    chatLayout.editText.text.clear()
  }
  
  override fun showOtherUserCancelledRequest() {
    textView("Text").text("User cancelled request")
    Timber.d("User cancelled")
  }
  
  override fun showOtherUserLeftChatting() {
    textView("DialogTitle").text("Other user left. \nDo you want to quit?")
    //    textView("DialogYes").onClick { presenter.exit() }
    textView("DialogNo").onClick { viewAs<SimpleDialog>().hide() }
    viewAs<SimpleDialog>().show()
  }
  
  override fun showExitRequestDialog() {
    textView("DialogTitle").text("Do you really want to quit?")
    //    textView("DialogYes").onClick { presenter.exit() }
    textView("DialogNo").onClick { viewAs<SimpleDialog>().hide() }
    viewAs<SimpleDialog>().show()
  }
  
  override fun showExit() {
    navigator.popCurrentScreen(notifyBackPress = false)
  }
  
  //  override fun onBackPressed(): Boolean {
  //    return !presenter.allowBackPress()
  //  }
  
  companion object {
    
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_TYPE = "KEY_TYPE"
    const val TYPE_JOINED = "TYPE_JOINED"
    const val TYPE_REQUEST = "TYPE_REQUEST"
  }
}