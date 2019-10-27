package com.arsvechkarev.users.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.OtherUser
import com.arsvechkarev.users.R
import com.arsvechkarev.users.di.DaggerUsersComponent
import com.arsvechkarev.users.list.UsersListAdapter
import kotlinx.android.synthetic.main.fragment_users.recyclerUsers
import kotlinx.android.synthetic.main.fragment_users.toolbar
import javax.inject.Inject

class UsersDialogFragment : DialogFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var usersViewModel: UsersViewModel
  
  private lateinit var usersClickListener: UsersClickListener
  private val adapter: UsersListAdapter by lazy { UsersListAdapter(usersClickListener::onUserSelected) }
  
  interface UsersClickListener {
    fun onUserSelected(otherUser: OtherUser)
  }
  
  override fun onAttach(context: Context) {
    super.onAttach(context)
    usersClickListener = parentFragment as? UsersClickListener
      ?: throw IllegalStateException("Parent fragment should implement UsersClickListener")
  }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(STYLE_NORMAL, R.style.DialogFullScreen)
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
      dismiss()
    }
    usersViewModel = viewModel(viewModelFactory) {
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