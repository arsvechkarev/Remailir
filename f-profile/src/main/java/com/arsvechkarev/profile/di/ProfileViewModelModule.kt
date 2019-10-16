package com.arsvechkarev.profile.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.profile.presentation.ProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  internal abstract fun postSignUpViewModel(viewModel: ProfileViewModel): ViewModel
}