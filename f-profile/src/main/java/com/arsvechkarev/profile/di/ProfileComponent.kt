package com.arsvechkarev.profile.di

import com.arsvechkarev.core.di.FeatureScope
import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.storage.di.StorageModule
import dagger.Component

@Component(
  modules = [
    StorageModule::class,
    ProfileViewModelModule::class,
    CoreViewModelModule::class
  ]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}