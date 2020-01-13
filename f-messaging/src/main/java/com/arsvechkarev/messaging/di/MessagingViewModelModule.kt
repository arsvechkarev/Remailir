package com.arsvechkarev.messaging.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.messaging.presentation.MessagingViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MessagingViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(MessagingViewModel::class)
  internal abstract fun postMessaging(viewModel: MessagingViewModel): ViewModel
  
}