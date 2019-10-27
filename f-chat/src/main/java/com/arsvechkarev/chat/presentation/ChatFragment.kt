package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.chat.R
import com.arsvechkarev.chat.di.DaggerChatComponent
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.chat.list.toDisplayableItems
import com.arsvechkarev.chat.presentation.MessagesState.Failure
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.setChatView
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.core.model.messaging.DialogMessage
import kotlinx.android.synthetic.main.fragment_chat.button
import kotlinx.android.synthetic.main.fragment_chat.editText
import kotlinx.android.synthetic.main.fragment_chat.recyclerChat
import kotlinx.android.synthetic.main.fragment_chat.textTest
import javax.inject.Inject

class ChatFragment : Fragment() {
  
  private val chatAdapter = ChatAdapter()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ChatViewModel
  private lateinit var otherUser: OtherUser
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_chat, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerChatComponent.create().inject(this)
    viewModel = viewModel(viewModelFactory) {
      observe(messages, ::updateMessagesList)
    }
    otherUser = arguments!!.getParcelable(FRIEND)
    prepareView()
    viewModel.fetchMessagesList(otherUser)
    button.setOnClickListener {
      viewModel.sendMessage(editText.string(), otherUser)
    }
    recyclerChat.setChatView(chatAdapter)
  }
  
  private fun prepareView() {
    textTest.text = otherUser.toString()
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
