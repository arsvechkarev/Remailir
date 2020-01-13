package com.arsvechkarev.chats.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.chats.presentation.ChatsViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ChatsViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(ChatsViewModel::class)
  internal abstract fun postChatViewModel(viewModel: ChatsViewModel): ViewModel
  
}