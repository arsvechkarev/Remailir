package com.arsvechkarev.messaging.di

import com.arsvechkarev.messaging.presentation.MessagingFragment
import core.di.CoreComponent
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ViewModelModule::class,
    MessagingViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface MessagingComponent {
  
  fun inject(fragment: MessagingFragment)
}