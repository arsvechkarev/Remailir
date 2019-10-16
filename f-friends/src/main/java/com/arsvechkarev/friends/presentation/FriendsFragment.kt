package com.arsvechkarev.friends.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.core.MainActivity
import com.arsvechkarev.core.MainFragment
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.friends.R
import com.arsvechkarev.friends.di.DaggerFriendsComponent
import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.friends.list.FriendsListAdapter
import kotlinx.android.synthetic.main.fragment_friends.recyclerFriends
import javax.inject.Inject

class FriendsFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  
  private lateinit var friendsViewModel: FriendsViewModel
  private val friendsListAdapter = FriendsListAdapter(::goToChat)
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_friends, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    injectThis()
    friendsViewModel = viewModel(viewModelFactory) {
      observe(friends, ::updateList)
      observe(chatStatus, ::reactToChatCreation)
    }
    friendsViewModel.fetchFriends()
    recyclerFriends.layoutManager = LinearLayoutManager(context!!)
    recyclerFriends.adapter = friendsListAdapter
  }
  
  private fun injectThis() {
    DaggerFriendsComponent.create().inject(this)
  }
  
  private fun updateList(list: List<Friend>) {
    friendsListAdapter.submitList(list)
  }
  
  private fun reactToChatCreation(chatStatus: ChatStatus) {
    when (chatStatus) {
      is ChatStatus.Success -> {
        (activity as MainActivity).goToChat(chatStatus.friend)
      }
      is ChatStatus.Failure -> {
        showToast("Error creating chat")
      }
    }
  }
  
  private fun goToChat(friend: Friend) {
    friendsViewModel.createOneToOneChat(friend)
  }
}
