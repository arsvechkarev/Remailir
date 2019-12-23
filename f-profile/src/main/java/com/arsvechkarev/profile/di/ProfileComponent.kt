package com.arsvechkarev.profile.di

import core.di.FeatureScope
import core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.profile.presentation.ProfileFragment
import dagger.Component

@Component(
  modules = [
    ProfileViewModelModule::class,
    CoreViewModelModule::class
  ]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}