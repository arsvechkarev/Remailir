package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.presentation.countrycodes.CountriesViewModel
import com.arsvechkarev.auth.presentation.countrycodes.SearchCountryFragment
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
  fun inject(fragment: CountriesFragment)
  fun inject(fragment: SearchCountryFragment)
}

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