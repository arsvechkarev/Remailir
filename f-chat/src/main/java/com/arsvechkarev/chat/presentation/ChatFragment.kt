package com.arsvechkarev.chat.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chat.R
import com.arsvechkarev.chat.di.DaggerChatComponent
import com.arsvechkarev.chat.list.ChatAdapter
import com.arsvechkarev.chat.presentation.MessagesState.Failure
import com.arsvechkarev.chat.presentation.MessagesState.MessagesList
import com.arsvechkarev.chat.presentation.MessagesState.SingleMessage
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.string
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.core.model.messaging.DialogMessage
import com.arsvechkarev.core.model.messaging.MessageThisUser
import com.arsvechkarev.core.model.messaging.MessageOtherUser
import com.arsvechkarev.core.recycler.DisplayableItem
import com.arsvechkarev.firebase.thisUserId
import kotlinx.android.synthetic.main.fragment_chat.button
import kotlinx.android.synthetic.main.fragment_chat.editText
import kotlinx.android.synthetic.main.fragment_chat.recyclerChat
import kotlinx.android.synthetic.main.fragment_chat.textTest
import java.util.UUID
import javax.inject.Inject


const val FRIEND = "FRIEND"

fun createChatFragment(friend: Friend): ChatFragment {
  val bundle = Bundle()
  bundle.putParcelable(FRIEND, friend)
  val fragment = ChatFragment()
  fragment.arguments = bundle
  return fragment
}

class ChatFragment : Fragment() {
  
  private val chatAdapter = ChatAdapter()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var chatViewModel: ChatViewModel
  
  private lateinit var friend: Friend
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_chat, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerChatComponent.create().inject(this)
    chatViewModel = viewModel(viewModelFactory) {
      observe(messages, ::updateMessagesList)
    }
    
    friend = arguments!!.getParcelable(FRIEND)
    textTest.text = friend.toString()
    
    chatViewModel.fetchMessagesList(friend)
    
    button.setOnClickListener {
      chatViewModel.sendMessage(editText.string(), friend)
    }
  
    val linearLayoutManager = LinearLayoutManager(context)
    linearLayoutManager.stackFromEnd = true
    recyclerChat.layoutManager = linearLayoutManager
    recyclerChat.adapter = chatAdapter
  }
  
  private fun updateMessagesList(state: MessagesState) {
    when (state) {
      is MessagesList -> { updateList(state.messages) }
      is Failure -> showToast("Houston, we have a problem")
    }
    
  }
  
  private fun updateList(messages: List<DialogMessage>) {
    val displayMessages = ArrayList<DisplayableItem>()
    messages.forEach {
      when (thisUserId) {
        it.fromUserId -> displayMessages.add(MessageThisUser(UUID.randomUUID().toString(), it.text))
        it.toUserId -> displayMessages.add(MessageOtherUser(UUID.randomUUID().toString(), it.text))
      }
    }
    chatAdapter.submitList(displayMessages)
  }
  
}
