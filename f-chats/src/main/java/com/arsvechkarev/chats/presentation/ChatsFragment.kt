package com.arsvechkarev.chats.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chats.R
import com.arsvechkarev.chats.list.ChatsAdapter
import com.arsvechkarev.messaging.presentation.MessagingFragment
import com.arsvechkarev.users.presentation.UsersListFragment
import core.base.coreActivity
import kotlinx.android.synthetic.main.fragment_messages.fabNewChat
import kotlinx.android.synthetic.main.fragment_messages.recyclerChats
import kotlinx.android.synthetic.main.fragment_messages.toolbar
import javax.inject.Inject

class ChatsFragment : Fragment() {
  
  private val adapter = ChatsAdapter {
    coreActivity.goToFragmentFromRoot(MessagingFragment.create(it.otherUser), true)
  }
  
  @Inject
  lateinit var viewFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ChatsViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_messages, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  
    toolbar.inflateMenu(R.menu.menu_main)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.itemSignOut -> {
          coreActivity.signOut()
          return@setOnMenuItemClickListener true
        }
      }
      return@setOnMenuItemClickListener false
    }
    fabNewChat.setOnClickListener {
      coreActivity.goToFragmentFromRoot(UsersListFragment(), true)
    }
  
    recyclerChats.layoutManager = LinearLayoutManager(context!!)
    recyclerChats.adapter = adapter
  
    //    MessagesRepository().fetchMessages {
    //      onSuccess {
    //        adapter.submitList(it)
    //      }
    //      onFailure {
    //      }
    //    }
    
  }
}
