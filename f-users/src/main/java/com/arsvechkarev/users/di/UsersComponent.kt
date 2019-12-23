package com.arsvechkarev.users.di

import com.arsvechkarev.users.presentation.UsersListFragment
import core.di.CoreModule
import dagger.Component

@Component(
  modules = [
    CoreModule::class,
    UsersViewModelModule::class
  ]
)
interface UsersComponent {
  
  fun inject(fragment: UsersListFragment)
}