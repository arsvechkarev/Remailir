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
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.extensions.invisible
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.onTextChanged
import com.arsvechkarev.core.extensions.popBackStack
import com.arsvechkarev.core.extensions.setChatView
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.extensions.visible
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.users.OtherUser
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
    viewModel = viewModel(viewModelFactory) {
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
  
    editText.onTextChanged {
      if (it.isBlank()) buttonSend.invisible()
      else buttonSend.visible()
    }
  
    Picasso.get()
      .load("https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg")
      .into(imageOtherUser)
  }
  
  private fun prepareView() {
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
    chatAdapter.submitList(messages.toDisplayableItems())
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
