package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.chat.R
import com.arsvechkarev.chat.di.DaggerChatComponent
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.chat.list.toDisplayableItems
import com.arsvechkarev.chat.presentation.MessagesState.Failure
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import core.base.BaseFragment
import core.extensions.afterTextChanged
import core.extensions.invisible
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.setChatView
import core.extensions.showToast
import core.extensions.string
import core.extensions.viewModelOf
import core.extensions.visible
import core.model.messaging.DialogMessage
import core.model.users.OtherUser
import firebase.DEFAULT_IMG_URL
import com.squareup.picasso.Picasso
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
  private lateinit var otherUser: OtherUser
  
  override val layout: Int = R.layout.fragment_chat
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerChatComponent.create().inject(this)
    imageBack.setOnClickListener { popBackStack() }
    viewModel = viewModelOf(viewModelFactory) {
      observe(messages, ::updateMessagesList)
    }
    otherUser = arguments!!.getParcelable(FRIEND)
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
  
    editText.afterTextChanged {
      if (it.isBlank()) buttonSend.invisible()
      else buttonSend.visible()
    }
  }
  
  private fun prepareView() {
    if (otherUser.imageUrl == DEFAULT_IMG_URL) {
      imageOtherUser.setBackgroundResource(R.drawable.image_stub)
    } else {
      Picasso.get().load(otherUser.imageUrl).into(imageOtherUser)
    }
    textOtherUserName.text = otherUser.username
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
    
    const val FRIEND = "FRIEND"
    
    fun newInstance(otherUser: OtherUser): ChatFragment {
      val bundle = Bundle()
      bundle.putParcelable(FRIEND, otherUser)
      val fragment = ChatFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}
