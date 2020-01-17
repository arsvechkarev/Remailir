package com.arsvechkarev.users.di

import com.arsvechkarev.users.presentation.UsersListFragment
import core.di.CoreComponent
import core.di.modules.ViewModelModule
import core.di.scopes.FeatureScope
import dagger.Component


@Component(
  modules = [
    ViewModelModule::class,
    UsersViewModelModule::class
  ],
  dependencies = [CoreComponent::class]
)
@FeatureScope
interface UsersComponent {
  
  fun inject(fragment: UsersListFragment)
}
