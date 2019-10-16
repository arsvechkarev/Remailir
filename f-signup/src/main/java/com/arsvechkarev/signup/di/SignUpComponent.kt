package com.arsvechkarev.signup.di

import com.arsvechkarev.core.di.FeatureScope
import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.signup.presentation.SignUpFragment
import dagger.Component

@Component(
  modules = [
    CoreViewModelModule::class,
    SignUpViewModelModule::class
  ]
)
@FeatureScope
interface SignUpComponent {
  
  fun inject(fragment: SignUpFragment)
}