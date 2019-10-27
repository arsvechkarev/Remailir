package com.arsvechkarev.messages.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arsvechkarev.chat.presentation.ChatFragment
import com.arsvechkarev.core.base.CoreActivity
import com.arsvechkarev.core.base.coreActivity
import com.arsvechkarev.core.extensions.setTitle
import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.messages.R
import com.arsvechkarev.users.presentation.UsersDialogFragment
import com.arsvechkarev.users.presentation.UsersDialogFragment.UsersClickListener
import kotlinx.android.synthetic.main.fragment_messages.fabNewChat
import kotlinx.android.synthetic.main.fragment_messages.toolbar

class MessagesFragment : Fragment(), UsersClickListener {

  private val usersDialogFragment = UsersDialogFragment()
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_messages, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setTitle(R.string.title_messages)
    toolbar.inflateMenu(R.menu.menu_main)
    toolbar.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.itemSignOut -> {
          (activity as CoreActivity).signOut()
          return@setOnMenuItemClickListener true
        }
      }
      return@setOnMenuItemClickListener false
    }
    fabNewChat.setOnClickListener {
      usersDialogFragment.show(childFragmentManager, null)
    }
  }
  
  override fun onUserSelected(otherUser: OtherUser) {
    usersDialogFragment.dismiss()
    coreActivity.goToFragmentFromRoot(ChatFragment.newInstance(otherUser), true)
  }
  
}
