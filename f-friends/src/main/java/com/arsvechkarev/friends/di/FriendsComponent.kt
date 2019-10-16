package com.arsvechkarev.friends.di

import com.arsvechkarev.core.di.viewmodel.CoreViewModelModule
import com.arsvechkarev.friends.presentation.FriendsFragment
import dagger.Component

@Component(
  modules = [
    CoreViewModelModule::class,
    FriendsViewModelModule::class
  ]
)
interface FriendsComponent {
  
  fun inject(fragment: FriendsFragment)
}