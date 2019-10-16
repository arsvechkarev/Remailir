package com.arsvechkarev.profile.di

import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.profile.presentation.ProfileFragment
import dagger.Component

@Component(
  modules = [
    ProfileViewModelModule::class,
    CoreViewModelModule::class
  ]
)
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}