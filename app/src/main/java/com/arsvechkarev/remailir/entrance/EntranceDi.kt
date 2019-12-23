package com.arsvechkarev.remailir.entrance

import androidx.lifecycle.ViewModel
import core.di.CoreModule
import core.di.FeatureScope
import core.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap
import firebase.di.FirebaseModule

@Component(modules = [CoreModule::class, FirebaseModule::class, EntranceViewModelModule::class])
@FeatureScope
interface EntranceComponent {
  
  fun inject(fragment: EntranceActivity)
}

@Module
abstract class EntranceViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(EntranceViewModel::class)
  internal abstract fun postEntranceViewModel(viewModel: EntranceViewModel): ViewModel
  
}