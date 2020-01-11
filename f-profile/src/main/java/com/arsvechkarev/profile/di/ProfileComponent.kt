package com.arsvechkarev.profile.di

import com.arsvechkarev.profile.presentation.ProfileFragment
import core.di.CoreModule
import core.di.scopes.FeatureScope
import dagger.Component
import firebase.di.FirebaseModule

@Component(
  modules = [
    ProfileViewModelModule::class,
    CoreModule::class,
    FirebaseModule::class
  ]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}