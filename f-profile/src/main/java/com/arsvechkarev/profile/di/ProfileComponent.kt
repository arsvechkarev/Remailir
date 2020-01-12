package com.arsvechkarev.profile.di

import com.arsvechkarev.profile.presentation.ProfileFragment
import core.di.CoreComponent
import core.di.modules.ContextModule
import core.di.modules.PicassoModule
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ProfileViewModelModule::class,
    ViewModelModule::class,
    ContextModule::class,
    PicassoModule::class
  ], dependencies = [CoreComponent::class]
)
@FeatureScope
interface ProfileComponent {
  
  fun inject(fragment: ProfileFragment)
}