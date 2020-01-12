package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.chat.R
import com.arsvechkarev.chat.di.DaggerChatComponent
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.chat.list.toDisplayableItems
import com.arsvechkarev.chat.presentation.MessagesState.Failure
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import com.squareup.picasso.Picasso
import core.base.BaseFragment
import core.model.messaging.DialogMessage
import core.model.users.User
import core.strings.DEFAULT_IMG_URL
import core.util.invisible
import core.util.observe
import core.util.popBackStack
import core.util.setChatView
import core.util.showToast
import core.util.string
import core.util.viewModelOf
import core.util.visible
import kotlinx.android.synthetic.main.fragment_chat.buttonSend
import kotlinx.android.synthetic.main.fragment_chat.editText
import kotlinx.android.synthetic.main.fragment_chat.imageBack
import kotlinx.android.synthetic.main.fragment_chat.imageOtherUser
import kotlinx.android.synthetic.main.fragment_chat.recyclerChat
import kotlinx.android.synthetic.main.fragment_chat.textOtherUserExtraInfo
import kotlinx.android.synthetic.main.fragment_chat.textOtherUserName
import javax.inject.Inject

class ChatFragment : BaseFragment() {
  
  private val chatAdapter = ChatAdapter()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ChatViewModel
  private lateinit var otherUser: User
  
  override val layout: Int = R.layout.fragment_chat
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerChatComponent.create().inject(this)
    imageBack.setOnClickListener { popBackStack() }
    viewModel = viewModelOf(viewModelFactory) {
      observe(messages, ::updateMessagesList)
    }
    otherUser = arguments!!.getParcelable(USER) as User
    prepareView()
    viewModel.fetchMessagesList(otherUser)
    buttonSend.setOnClickListener {
      viewModel.sendMessage(editText.string(), otherUser)
      editText.text.clear()
    }
    recyclerChat.setChatView(chatAdapter)
    recyclerChat.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
      if (bottom < oldBottom) {
        recyclerChat.post {
          recyclerChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
      }
    }
  
    editText.doAfterTextChanged {
      if (it.isNullOrBlank()) buttonSend.invisible()
      else buttonSend.visible()
    }
  }
  
  private fun prepareView() {
    if (otherUser.imageUrl == DEFAULT_IMG_URL) {
      imageOtherUser.setBackgroundResource(R.drawable.image_profile_stub)
    } else {
      Picasso.get().load(otherUser.imageUrl).into(imageOtherUser)
    }
    textOtherUserName.text = otherUser.name
    textOtherUserExtraInfo.text = otherUser.id
  }
  
  private fun updateMessagesList(state: MessagesState) {
    when (state) {
      is MessagesList -> {
        updateList(state.messages)
      }
      is Failure -> showToast("Houston, we have a problem")
    }
  }
  
  private fun updateList(messages: List<DialogMessage>) {
    recyclerChat.post {
      chatAdapter.submitList(messages.toDisplayableItems())
      recyclerChat.scrollToPosition(chatAdapter.itemCount - 1)
    }
  }
  
  companion object {
  
    const val USER = "USER"
  
    fun create(user: User) = ChatFragment().apply {
      arguments = Bundle().apply {
        putParcelable(USER, user)
      }
    }
  }
}
