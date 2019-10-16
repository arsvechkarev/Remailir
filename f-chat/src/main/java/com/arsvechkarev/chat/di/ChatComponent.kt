package com.arsvechkarev.chat.di

import com.arsvechkarev.chat.presentation.ChatFragment
import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import dagger.Component

@Component(
  modules = [
    CoreViewModelModule::class,
    ChatViewModelModule::class
  ]
)
interface ChatComponent {
  
  fun inject(fragment: ChatFragment)
}