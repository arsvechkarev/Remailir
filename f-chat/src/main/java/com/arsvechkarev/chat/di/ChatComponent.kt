package com.arsvechkarev.chat.di

import com.arsvechkarev.chat.presentation.ChatFragment
import core.di.CoreModule
import dagger.Component

@Component(
  modules = [
    CoreModule::class,
    ChatViewModelModule::class
  ]
)
interface ChatComponent {
  
  fun inject(fragment: ChatFragment)
}