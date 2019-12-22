package com.arsvechkarev.remailir.main.registration

import androidx.lifecycle.ViewModel
import com.arsvechkarev.core.di.CoreModule
import com.arsvechkarev.core.di.FeatureScope
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.firebase.di.FirebaseModule
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