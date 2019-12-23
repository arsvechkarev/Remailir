package com.arsvechkarev.remailir.main.registration

import androidx.lifecycle.ViewModel
import core.di.CoreModule
import core.di.FeatureScope
import core.di.viewmodel.ViewModelKey
import firebase.di.FirebaseModule
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

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