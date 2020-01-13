package com.arsvechkarev.chats.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chats.R
import com.arsvechkarev.chats.di.DaggerChatsComponent
import com.arsvechkarev.chats.list.ChatsAdapter
import com.arsvechkarev.messaging.presentation.MessagingFragment
import com.arsvechkarev.users.presentation.UsersListFragment
import core.MaybeResult
import core.base.coreActivity
import core.di.coreComponent
import core.extensions.COLOR_PROGRESS_CIRCLE
import core.extensions.COLOR_PROGRESS_CIRCLE_BG
import core.extensions.observe
import core.extensions.showToast
import core.extensions.viewModelOf
import core.model.messaging.Chat
import kotlinx.android.synthetic.main.fragment_chats.fabNewChat
import kotlinx.android.synthetic.main.fragment_chats.recyclerChats
import kotlinx.android.synthetic.main.fragment_chats.swipeRefreshChatsLayout
import log.log
import javax.inject.Inject

class ChatsFragment : Fragment() {
  
  private val adapter = ChatsAdapter {
    coreActivity.goToFragmentFromRoot(MessagingFragment.create(it.otherUser), true)
  }
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ChatsViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_chats, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    swipeRefreshChatsLayout.setColorSchemeColors(COLOR_PROGRESS_CIRCLE)
    swipeRefreshChatsLayout.setProgressBackgroundColorSchemeColor(COLOR_PROGRESS_CIRCLE_BG)
    DaggerChatsComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(chatState, ::handleChatState)
      fetchMessages()
    }
    swipeRefreshChatsLayout.setOnRefreshListener {
      viewModel.fetchMessages()
    }
    fabNewChat.setOnClickListener {
      coreActivity.goToFragmentFromRoot(UsersListFragment(), true)
    }
    recyclerChats.layoutManager = LinearLayoutManager(context!!)
    recyclerChats.adapter = adapter
  }
  
  private fun handleChatState(result: MaybeResult<List<Chat>>) {
    swipeRefreshChatsLayout.isRefreshing = false
    when {
      result.isSuccess -> {
        adapter.submitList(result.data)
      }
      result.isNothing -> {
        adapter.submitList(null)
        showToast("No chats yet")
      }
      result.isFailure -> {
        log(result.exception) { "Error chats" }
        showToast("Error while downloading chats")
      }
    }
  }
}
