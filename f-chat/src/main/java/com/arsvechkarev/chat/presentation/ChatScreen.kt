package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.Gravity
import com.arsvechkarev.chat.domain.ChatRepository
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.model.messaging.MessageFactoryImpl
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.firestore.chatmanaging.ChatFirebaseDataSource
import com.arsvechkarev.firebase.firestore.messaging.FirebaseMessagingDataSource
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.addView
import com.arsvechkarev.viewdsl.childView
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.ChatLayout
import timber.log.Timber

class ChatScreen : Screen(), ChatView {
  
  override fun buildLayout() = withViewBuilder {
    RootCoordinatorLayout {
      addView {
        ChatLayout(context).apply {
          tag("ChatMainLayout")
          size(MatchParent, MatchParent)
          imageSend.onClick {
            if (editText.text.toString().isNotBlank()) {
              presenter.sendMessage(editText.text.toString())
            }
          }
        }
      }
      TextView(WrapContent, WrapContent, style = Styles.BoldTextView) {
        tag("Text")
        textSize(TextSizes.H2)
        layoutGravity(Gravity.CENTER)
      }
    }
  }
  
  private val presenter by moxyPresenter {
    ChatPresenter(
      FirebaseAuthenticator.getUsername(),
      MessageFactoryImpl,
      ChatRepository(
        ChatFirebaseDataSource(
          FirebaseAuthenticator.getUsername(),
          AndroidDispatchers,
        ),
        FirebaseMessagingDataSource(
          FirebaseAuthenticator.getUsername(),
          arguments!!.getString(KEY_USERNAME)!!,
          AndroidDispatchers
        )
      ),
      AndroidDispatchers
    )
  }
  
  private val chatLayout get() = viewNonNull.childView("ChatMainLayout") as ChatLayout
  
  private val adapter = ChatAdapter()
  
  override fun onInit(arguments: Bundle) {
    val otherUserUsername = arguments.getString(KEY_USERNAME)!!
    chatLayout.toolbar.title(otherUserUsername)
    chatLayout.recyclerView.adapter = adapter
    when (arguments.getString(KEY_TYPE)) {
      TYPE_JOINED -> {
        textView("Text").text("Joined for chat")
        presenter.startListeningForMessages()
      }
      TYPE_REQUEST -> {
        textView("Text").text("Waiting for joining")
        presenter.start(otherUserUsername)
      }
      else -> throw IllegalStateException()
    }
  }
  
  override fun showUserJoined() {
    Timber.d("User joined")
    textView("Text").text("Joined")
    presenter.startListeningForMessages()
  }
  
  override fun showMessageReceived(messageOtherUser: MessageOtherUser) {
    adapter.addItem(messageOtherUser)
  }
  
  override fun showMessageSent(messageThisUser: MessageThisUser) {
    adapter.addItem(messageThisUser)
    chatLayout.editText.text.clear()
  }
  
  override fun showUserCancelledJoiningRequest() {
    textView("Text").text("User cancelled request")
    Timber.d("User cancelled")
  }
  
  companion object {
    
    const val KEY_USERNAME = "KEY_USERNAME"
    const val KEY_TYPE = "KEY_TYPE"
    const val TYPE_JOINED = "TYPE_JOINED"
    const val TYPE_REQUEST = "TYPE_REQUEST"
  }
}