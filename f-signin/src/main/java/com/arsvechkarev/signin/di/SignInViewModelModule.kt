package com.arsvechkarev.signin.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.signin.presentation.SignInViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignInViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(SignInViewModel::class)
  internal abstract fun postSignInViewModel(viewModel: SignInViewModel): ViewModel
  
}