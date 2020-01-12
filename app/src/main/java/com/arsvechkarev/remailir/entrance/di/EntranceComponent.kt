package com.arsvechkarev.remailir.entrance.di

import com.arsvechkarev.remailir.entrance.EntranceActivity
import core.di.CoreComponent
import core.di.modules.ContextModule
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ContextModule::class,
    ViewModelModule::class,
    EntranceViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface EntranceComponent {
  
  fun inject(fragment: EntranceActivity)
}