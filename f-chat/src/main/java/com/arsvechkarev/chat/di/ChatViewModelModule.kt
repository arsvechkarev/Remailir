package com.arsvechkarev.chat.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.chat.presentation.ChatViewModel
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(ChatViewModel::class)
  internal abstract fun postChatViewModel(viewModel: ChatViewModel): ViewModel
  
}