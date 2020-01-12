package com.arsvechkarev.chat.di

import com.arsvechkarev.chat.presentation.MessagingFragment
import core.di.modules.CoreModule
import dagger.Component

@Component(
  modules = [
    CoreModule::class,
    MessagingViewModelModule::class
  ]
)
interface MessagingComponent {
  
  fun inject(fragment: MessagingFragment)
}