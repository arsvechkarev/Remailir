package com.arsvechkarev.profile.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.profile.presentation.ProfileViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(ProfileViewModel::class)
  internal abstract fun postProfileViewModel(viewModel: ProfileViewModel): ViewModel
}