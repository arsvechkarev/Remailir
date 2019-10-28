package com.arsvechkarev.messages.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arsvechkarev.core.declaration.coreActivity
import com.arsvechkarev.messages.R
import com.arsvechkarev.users.presentation.UsersListFragment
import kotlinx.android.synthetic.main.fragment_messages.fabNewChat
import kotlinx.android.synthetic.main.fragment_messages.toolbar

class MessagesFragment : Fragment() {

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
  }
}
