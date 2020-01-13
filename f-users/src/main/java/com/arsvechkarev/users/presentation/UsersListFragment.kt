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
import core.extensions.COLOR_ACCENT
import core.extensions.COLOR_PRIMARY
import core.extensions.gone
import core.extensions.observe
import core.extensions.popBackStack
import core.extensions.showToast
import core.extensions.viewModelOf
import core.extensions.visible
import core.model.users.User
import core.whenFailure
import core.whenNothing
import core.whenSuccess
import kotlinx.android.synthetic.main.fragment_users.progressBarUsers
import kotlinx.android.synthetic.main.fragment_users.recyclerUsers
import kotlinx.android.synthetic.main.fragment_users.swipeRefreshUsersLayout
import kotlinx.android.synthetic.main.fragment_users.theToolbar
import log.log
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
    swipeRefreshUsersLayout.setColorSchemeColors(COLOR_ACCENT)
    swipeRefreshUsersLayout.setProgressBackgroundColorSchemeColor(COLOR_PRIMARY)
    DaggerUsersComponent.create().inject(this)
    theToolbar.setNavigationOnClickListener {
      popBackStack()
    }
    viewModel = viewModelOf(viewModelFactory) {
      observe(usersListData, ::updateList)
      observe(chatCreationState, ::handleState)
    }
    swipeRefreshUsersLayout.setOnRefreshListener {
      fetchUsers(showProgressBar = false)
    }
    recyclerUsers.adapter = adapter
    recyclerUsers.layoutManager = LinearLayoutManager(context)
    fetchUsers()
  }
  
  private fun fetchUsers(showProgressBar: Boolean = true) {
    viewModel.fetchUsers()
    if (showProgressBar) {
      progressBarUsers.visible()
    }
  }
  
  private fun updateList(result: MaybeResult<List<User>>) {
    swipeRefreshUsersLayout.isRefreshing = false
    progressBarUsers.gone()
    result.whenSuccess {
      adapter.submitList(it)
    }
    result.whenFailure {
      showToast("Error")
      log(it)
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