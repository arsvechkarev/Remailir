package com.arsvechkarev.friends.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.friends.presentation.FriendsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class FriendsViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(FriendsViewModel::class)
  internal abstract fun postFriendsViewModel(viewModel: FriendsViewModel): ViewModel
  
}