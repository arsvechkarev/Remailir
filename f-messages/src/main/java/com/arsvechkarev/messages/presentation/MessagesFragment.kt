package com.arsvechkarev.messages.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chat.presentation.ChatFragment
import com.arsvechkarev.core.declaration.coreActivity
import com.arsvechkarev.messages.R
import com.arsvechkarev.messages.list.MessagesAdapter
import com.arsvechkarev.messages.repository.MessagesRepository
import com.arsvechkarev.users.presentation.UsersListFragment
import kotlinx.android.synthetic.main.fragment_messages.fabNewChat
import kotlinx.android.synthetic.main.fragment_messages.recyclerChats
import kotlinx.android.synthetic.main.fragment_messages.toolbar

class MessagesFragment : Fragment() {
  
  private val adapter = MessagesAdapter {
    coreActivity.goToFragmentFromRoot(ChatFragment.newInstance(it.otherUser), true)
  }
  
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
  
    MessagesRepository().fetchMessages {
      onSuccess {
        Log.d("Chatting", it.toString())
        adapter.submitList(it)
      }
      onFailure {
        Log.d("Chatting", "Error", it)
      }
    }
    
  }
}
