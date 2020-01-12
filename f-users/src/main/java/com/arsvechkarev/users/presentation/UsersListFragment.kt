package com.arsvechkarev.users.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.chat.presentation.MessagingFragment
import com.arsvechkarev.users.R
import com.arsvechkarev.users.di.DaggerUsersComponent
import com.arsvechkarev.users.list.UsersListAdapter
import core.MaybeResult
import core.base.coreActivity
import core.model.users.User
import core.util.observe
import core.util.popBackStack
import core.util.showToast
import core.util.viewModelOf
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
  private lateinit var usersViewModel: UsersViewModel
  
  private val adapter = UsersListAdapter {
    popBackStack()
    coreActivity.goToFragmentFromRoot(MessagingFragment.create(it), true)
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
      observe(usersListData, ::updateList)
    }
    recyclerUsers.adapter = adapter
    recyclerUsers.layoutManager = LinearLayoutManager(context)
    usersViewModel.fetchUsers()
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
}