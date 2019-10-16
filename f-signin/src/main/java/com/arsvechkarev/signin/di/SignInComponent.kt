package com.arsvechkarev.signin.di

import com.arsvechkarev.core.di.FeatureScope
import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.signin.presentation.SignInFragment
import dagger.Component

@Component(
  modules = [
    CoreViewModelModule::class,
    SignInViewModelModule::class
  ]
)
@FeatureScope
interface SignInComponent {
  
  fun inject(fragment: SignInFragment)
}