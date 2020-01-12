package com.arsvechkarev.remailir.entrance.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.remailir.entrance.EntranceViewModel
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EntranceViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(EntranceViewModel::class)
  internal abstract fun postEntranceViewModel(viewModel: EntranceViewModel): ViewModel
}