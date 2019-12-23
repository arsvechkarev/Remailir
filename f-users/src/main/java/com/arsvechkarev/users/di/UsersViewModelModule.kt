package com.arsvechkarev.users.di

import androidx.lifecycle.ViewModel
import core.di.viewmodel.ViewModelKey
import com.arsvechkarev.users.presentation.UsersViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class UsersViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(UsersViewModel::class)
  internal abstract fun postUsersViewModel(viewModel: UsersViewModel): ViewModel
}