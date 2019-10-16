package com.arsvechkarev.remailir.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arsvechkarev.chat.presentation.createChatFragment
import com.arsvechkarev.core.MainFragment
import com.arsvechkarev.core.extensions.goToFragment
import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.friends.presentation.FriendsFragment
import com.arsvechkarev.messages.presentation.MessagesFragment
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.remailir.R
import kotlinx.android.synthetic.main.fragment_base.bottomNavigationBar

class BaseFragment : Fragment(), MainFragment {
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_base, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    goToFragment(R.id.mainContainer, ProfileFragment())
    bottomNavigationBar.selectedItemId = R.id.itemProfile
    bottomNavigationBar.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.itemFriends -> goToFragment(R.id.mainContainer, FriendsFragment())
        R.id.itemMessages -> goToFragment(
          R.id.mainContainer,
          MessagesFragment()
        )
        R.id.itemProfile -> goToFragment(R.id.mainContainer,
          ProfileFragment()
        )
      }
      return@setOnNavigationItemSelectedListener true
    }
  }
  
  override fun goToChat(friend: Friend) {
    bottomNavigationBar.selectedItemId = R.id.itemMessages
    goToFragment(android.R.id.content,
      createChatFragment(friend), true)
  }
}
