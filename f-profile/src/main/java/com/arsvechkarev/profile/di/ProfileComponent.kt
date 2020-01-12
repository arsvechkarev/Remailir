package com.arsvechkarev.profile.di

import com.arsvechkarev.profile.presentation.ProfileFragment
import core.di.modules.ContextModule
import core.di.modules.CoreModule
import core.di.modules.PicassoModule
import core.di.scopes.FeatureScope
import dagger.Component
import firebase.di.FirebaseModule

@Component(
  modules = [
    ProfileViewModelModule::class,
    CoreModule::class,
    FirebaseModule::class,
    ContextModule::class,
    PicassoModule::class
  ]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}