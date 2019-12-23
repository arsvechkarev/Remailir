package com.arsvechkarev.users.di

import core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.users.presentation.UsersListFragment
import dagger.Component

@Component(
  modules = [
    CoreViewModelModule::class,
    UsersViewModelModule::class
  ]
)
interface UsersComponent {
  
  fun inject(fragment: UsersListFragment)
}