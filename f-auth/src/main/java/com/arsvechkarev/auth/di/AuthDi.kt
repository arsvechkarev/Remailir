package com.arsvechkarev.auth.di

import androidx.lifecycle.ViewModel
import com.arsvechkarev.auth.presentation.fragments.SignInFragment
import com.arsvechkarev.auth.presentation.fragments.RegistrationFragment
import com.arsvechkarev.auth.presentation.viewmodels.SignInViewModel
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
  
  fun inject(fragment: SignInFragment)
  fun inject(fragment: RegistrationFragment)
}

@Module
abstract class AuthViewModelModule {
  
  @Binds
  @IntoMap
  @ViewModelKey(SignInViewModel::class)
  internal abstract fun postSignInViewModel(viewModel: SignInViewModel): ViewModel
  
  @Binds
  @IntoMap
  @ViewModelKey(RegistrationViewModel::class)
  internal abstract fun postRegisterViewModel(viewModel: RegistrationViewModel): ViewModel
  
}