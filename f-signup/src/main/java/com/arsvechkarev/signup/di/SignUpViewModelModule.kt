package com.arsvechkarev.signup.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.signup.presentation.SignUpViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SignUpViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(SignUpViewModel::class)
  internal abstract fun postSignUpViewModel(viewModel: SignUpViewModel): ViewModel
  
}