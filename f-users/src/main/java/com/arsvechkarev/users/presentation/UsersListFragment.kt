package com.arsvechkarev.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chat.presentation.ChatFragment
import core.declaration.coreActivity
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.viewModelOf
import core.model.users.OtherUser
import com.arsvechkarev.users.R
import com.arsvechkarev.users.di.DaggerUsersComponent
import com.arsvechkarev.users.list.UsersListAdapter
import kotlinx.android.synthetic.main.fragment_users.recyclerUsers
import kotlinx.android.synthetic.main.fragment_users.toolbar
import javax.inject.Inject

class UsersListFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var usersViewModel: UsersViewModel
  
  private val adapter: UsersListAdapter by lazy {
    UsersListAdapter {
      popBackStack()
      coreActivity.goToFragmentFromRoot(ChatFragment.newInstance(it), true)
    }
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_users, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    DaggerUsersComponent.create().inject(this)
    toolbar.setNavigationOnClickListener {
      popBackStack()
    }
    usersViewModel = viewModelOf(viewModelFactory) {
      observe(friends, ::updateList)
    }
    recyclerUsers.adapter = adapter
    recyclerUsers.layoutManager = LinearLayoutManager(context)
    usersViewModel.fetchUsers()
  }
  
  private fun updateList(list: List<OtherUser>) {
    adapter.submitList(list)
  }
}