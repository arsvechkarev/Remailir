package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.countrycodes.CountryCodeFragment
import com.arsvechkarev.auth.presentation.signup.RegistrationFragment
import com.arsvechkarev.auth.presentation.signup.RegistrationViewModel
import core.di.CoreModule
import core.di.FeatureScope
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import firebase.di.FirebaseModule

@Component(
  modules = [
    CoreModule::class,
    AuthViewModelModule::class,
    FirebaseModule::class
  ]
)
@FeatureScope
interface AuthComponent {
  
  fun inject(fragment: RegistrationFragment)
  fun inject(fragment: CountryCodeFragment)
}

@Module
abstract class AuthViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(RegistrationViewModel::class)
  internal abstract fun postRegisterViewModel(viewModel: RegistrationViewModel): ViewModel
  
}