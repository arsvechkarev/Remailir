package com.arsvechkarev.auth.di

import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.presentation.signup.RegistrationFragment
import core.di.CoreComponent
import core.di.modules.ContextModule
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ContextModule::class,
    ViewModelModule::class,
    AuthViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface AuthComponent {
  
  fun inject(fragment: CountriesFragment)
  fun inject(fragment: RegistrationFragment)
}