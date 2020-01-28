package com.arsvechkarev.auth.di

import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.presentation.phone.PhoneFragment
import com.arsvechkarev.auth.presentation.registration.RegistrationFragment
import com.arsvechkarev.auth.presentation.sms.SmsCodeFragment
import core.di.CoreComponent
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ViewModelModule::class,
    AuthViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface AuthComponent {
  
  fun inject(fragment: PhoneFragment)
  fun inject(fragment: CountriesFragment)
  fun inject(fragment: SmsCodeFragment)
  fun inject(fragment: RegistrationFragment)
}