package com.arsvechkarev.chat.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.chat.presentation.MessagingViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MessagingViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(MessagingViewModel::class)
  internal abstract fun postChatViewModel(viewModel: MessagingViewModel): ViewModel
  
}