package com.arsvechkarev.profile.presentation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.viewModel
import com.arsvechkarev.core.model.User
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileEmail
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
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
