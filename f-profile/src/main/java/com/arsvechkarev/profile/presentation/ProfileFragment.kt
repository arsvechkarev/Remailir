package com.arsvechkarev.profile.presentation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.core.base.ConfigFragment
import com.arsvechkarev.core.base.Configs
import com.arsvechkarev.core.base.CoreActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.setTitle
import com.arsvechkarev.core.extensions.showToast
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.User
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileEmail
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.toolbar
import javax.inject.Inject


class ProfileFragment : Fragment() {
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var profileViewModel: ProfileViewModel
  
  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_profile, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setTitle(R.string.title_profile)
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
    DaggerProfileComponent.create().inject(this)
    profileViewModel = viewModel(viewModelFactory) {
      observe(userData, ::updateProfile)
    }
    profileViewModel.fetchProfileData()
  }
  
  private fun updateProfile(user: User) {
    Picasso.get()
      .load("https://hatrabbits.com/wp-content/uploads/2017/01/random.jpg")
      .into(imageProfile)
    textProfileName.text = user.username
    textProfileEmail.text = user.email
  }
}
