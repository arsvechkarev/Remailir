package com.arsvechkarev.profile.di

import com.arsvechkarev.profile.presentation.ProfileFragment
import core.di.CoreModule
import core.di.FeatureScope
import dagger.Component

@Component(
  modules = [
    ProfileViewModelModule::class,
    CoreModule::class
  ]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}