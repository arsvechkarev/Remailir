package com.arsvechkarev.users.di

import com.arsvechkarev.users.presentation.UsersListFragment
import core.di.modules.CoreModule
import core.di.scopes.FeatureScope
import dagger.Component
import firebase.di.FirebaseModule

@Component(
  modules = [
    CoreModule::class,
    UsersViewModelModule::class,
    FirebaseModule::class
  ]
)
@FeatureScope
interface UsersComponent {
  
  fun inject(fragment: UsersListFragment)
}