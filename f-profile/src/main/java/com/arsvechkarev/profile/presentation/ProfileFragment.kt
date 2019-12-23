package com.arsvechkarev.profile.presentation

import androidx.lifecycle.ViewModelProvider
import core.base.BaseFragment
import core.declaration.coreActivity
import core.di.ContextModule
import core.extensions.observe
import core.extensions.viewModelOf
import core.model.users.User
import firebase.DEFAULT_IMG_URL
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
    inject()
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
    profileViewModel = viewModelOf(viewModelFactory) {
      observe(userData, ::updateProfile)
    }
    profileViewModel.fetchProfileData()
  }
  
  private fun inject() {
    DaggerProfileComponent.builder()
      .contextModule(ContextModule(context!!))
      .build()
      .inject(this)
  }
  
  private fun updateProfile(user: User) {
    if (user.imageUrl == DEFAULT_IMG_URL) {
      imageProfile.setBackgroundResource(R.drawable.image_stub)
    } else {
      Picasso.get().load(user.imageUrl).into(imageProfile)
    }
    textProfileName.text = user.username
    textProfileEmail.text = user.email
  }
}
