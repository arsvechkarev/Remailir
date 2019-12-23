package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.fragments.RegistrationFragment
import com.arsvechkarev.auth.presentation.viewmodels.RegistrationViewModel
import core.di.CoreModule
import core.di.FeatureScope
import core.di.viewmodel.ViewModelKey
import firebase.di.FirebaseModule
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.multibindings.IntoMap

@Component(
  modules = [
    CoreModule::class,
    AuthViewModelModule::class,
    FirebaseModule::class
  ]
)
@FeatureScope
interface AuthComponent {
  
  fun inject(fragment: RegistrationFragment)
}

@Module
abstract class AuthViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(RegistrationViewModel::class)
  internal abstract fun postRegisterViewModel(viewModel: RegistrationViewModel): ViewModel
  
}