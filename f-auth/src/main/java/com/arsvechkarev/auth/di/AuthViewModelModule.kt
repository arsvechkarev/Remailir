package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel
import com.arsvechkarev.auth.presentation.signup.RegistrationViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(RegistrationViewModel::class)
  internal abstract fun postRegisterViewModel(viewModel: RegistrationViewModel): ViewModel
  
  @Binds
  @IntoMap
  @ViewModelKey(CountriesViewModel::class)
  internal abstract fun postCountriesViewModel(viewModel: CountriesViewModel): ViewModel
  
}