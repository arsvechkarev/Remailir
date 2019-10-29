package com.arsvechkarev.profile.presentation


import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.core.base.BaseFragment
import com.arsvechkarev.core.declaration.CoreActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.users.ThisUser
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileEmail
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.toolbar
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var profileViewModel: ProfileViewModel
  
  override val layout: Int = R.layout.fragment_profile
  
  override fun onInit() {
    DaggerProfileComponent.create().inject(this)
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
    profileViewModel = viewModel(viewModelFactory) {
      observe(userData, ::updateProfile)
    }
    profileViewModel.fetchProfileData()
  }
  
  private fun updateProfile(thisUser: ThisUser) {
    Picasso.get()
      .load("https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg")
      .into(imageProfile)
    textProfileName.text = thisUser.username
    textProfileEmail.text = thisUser.email
  }
}
