package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.fragments.EnterFragment
import com.arsvechkarev.auth.presentation.fragments.PhoneFragment
import com.arsvechkarev.auth.presentation.fragments.RegistrationFragment
import com.arsvechkarev.auth.presentation.viewmodels.EnterViewModel
import com.arsvechkarev.auth.presentation.viewmodels.PhoneViewModel
import com.arsvechkarev.auth.presentation.viewmodels.RegistrationViewModel
import com.arsvechkarev.core.di.CoreModule
import com.arsvechkarev.core.di.FeatureScope
import com.arsvechkarev.core.di.viewmodel.ViewModelKey
import com.arsvechkarev.firebase.di.FirebaseModule
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
  
  fun inject(fragment: PhoneFragment)
  fun inject(fragment: EnterFragment)
  fun inject(fragment: RegistrationFragment)
}

@Module
abstract class AuthViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(PhoneViewModel::class)
  internal abstract fun postPhoneViewModel(viewModel: PhoneViewModel): ViewModel
  
  @Binds
  @IntoMap
  @ViewModelKey(EnterViewModel::class)
  internal abstract fun postEnterViewModel(viewModel: EnterViewModel): ViewModel
  
  @Binds
  @IntoMap
  @ViewModelKey(RegistrationViewModel::class)
  internal abstract fun postRegisterViewModel(viewModel: RegistrationViewModel): ViewModel
  
}