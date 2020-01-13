package com.arsvechkarev.messaging.presentation

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.messaging.R
import com.arsvechkarev.messaging.di.DaggerMessagingComponent
import com.arsvechkarev.messaging.list.MessagingAdapter
import com.arsvechkarev.messaging.presentation.MessagingState.FailedToSent
import com.arsvechkarev.messaging.presentation.MessagingState.Success
import com.squareup.picasso.Picasso
import core.MaybeResult
import core.base.BaseFragment
import core.di.coreComponent
import core.di.modules.ContextModule
import core.extensions.invisible
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.setChatView
import core.extensions.showToast
import core.extensions.string
import core.extensions.viewModelOf
import core.extensions.visible
import core.model.users.User
import core.recycler.DisplayableItem
import core.strings.DEFAULT_IMG_URL
import kotlinx.android.synthetic.main.fragment_chat.buttonSend
import kotlinx.android.synthetic.main.fragment_chat.editText
import kotlinx.android.synthetic.main.fragment_chat.imageBack
import kotlinx.android.synthetic.main.fragment_chat.imageOtherUser
import kotlinx.android.synthetic.main.fragment_chat.recyclerChat
import kotlinx.android.synthetic.main.fragment_chat.textOtherUserExtraInfo
import kotlinx.android.synthetic.main.fragment_chat.textOtherUserName
import log.log
import javax.inject.Inject

class MessagingFragment : BaseFragment() {
  
  private val chatAdapter = MessagingAdapter()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: MessagingViewModel
  private lateinit var otherUser: User
  
  override val layout: Int = R.layout.fragment_chat
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerMessagingComponent.builder()
      .coreComponent(coreComponent)
      .contextModule(ContextModule(context!!))
      .build()
      .inject(this)
    imageBack.setOnClickListener { popBackStack() }
    viewModel = viewModelOf(viewModelFactory) {
      observe(messagesState, ::updateList)
      observe(sendingMessageState, ::handleState)
    }
    otherUser = arguments!!.getParcelable(USER) as User
    prepareView()
    viewModel.fetchMessagesList(otherUser)
    buttonSend.setOnClickListener {
      viewModel.sendMessage(editText.string())
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
  
  private fun handleState(state: MessagingState) {
    when (state) {
      is Success -> {
      
      }
      is FailedToSent -> {
        showToast("Failed to sent message")
        log(state.error) { "Failed to sent message" }
      }
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
  
  private fun updateList(result: MaybeResult<List<DisplayableItem>>) {
    when {
      result.isSuccess -> {
        recyclerChat.post {
          chatAdapter.submitList(result.data)
          recyclerChat.scrollToPosition(chatAdapter.itemCount - 1)
        }
      }
      result.isNothing -> {
        showToast("no messages yet")
      }
      result.isFailure -> {
        showToast("something went wrong")
      }
    }
    
  }
  
  companion object {
  
    const val USER = "USER"
  
    fun create(user: User) = MessagingFragment().apply {
      arguments = Bundle().apply {
        putParcelable(USER, user)
      }
    }
  }
}
