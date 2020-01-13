package com.arsvechkarev.chats.di

import com.arsvechkarev.chats.presentation.ChatsFragment
import core.di.CoreComponent
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component

@Component(
  modules = [
    ViewModelModule::class,
    ChatsViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface ChatsComponent {
  
  fun inject(fragment: ChatsFragment)
}