package com.arsvechkarev.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.messaging.presentation.MessagingFragment
import com.arsvechkarev.users.R
import com.arsvechkarev.users.di.DaggerUsersComponent
import com.arsvechkarev.users.list.UsersListAdapter
import core.MaybeResult
import core.base.coreActivity
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.showToast
import core.extensions.viewModelOf
import core.model.users.User
import core.whenFailure
import core.whenNothing
import core.whenSuccess
import kotlinx.android.synthetic.main.fragment_users.recyclerUsers
import kotlinx.android.synthetic.main.fragment_users.toolbar
import log.debug
import javax.inject.Inject

class UsersListFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: UsersViewModel
  
  private val adapter = UsersListAdapter {
    viewModel.createChat(it)
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
    viewModel = viewModelOf(viewModelFactory) {
      observe(usersListData, ::updateList)
      observe(chatCreationState, ::handleState)
    }
    recyclerUsers.adapter = adapter
    recyclerUsers.layoutManager = LinearLayoutManager(context)
    viewModel.fetchUsers()
  }
  
  private fun updateList(result: MaybeResult<List<User>>) {
    result.whenSuccess {
      adapter.submitList(it)
    }
    result.whenFailure {
      debug(it)
    }
    result.whenNothing {
      showToast("No users")
    }
  }
  
  private fun handleState(result: Result<User>) {
    result.onSuccess {
      popBackStack()
      coreActivity.goToFragmentFromRoot(MessagingFragment.create(it), true)
    }
    result.onFailure {
      showToast("Failed to create chat")
    }
  }
}