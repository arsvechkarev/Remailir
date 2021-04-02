package com.arsvechkarev.remailir

import android.app.Application
import com.arsvechkarev.remailir.di.AppComponent
import com.arsvechkarev.remailir.di.DefaultProviders
import com.google.firebase.database.FirebaseDatabase
import core.di.AppDependenciesProvider

open class BaseRemailirApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    FirebaseDatabase.getInstance().setPersistenceEnabled(false)
    configureDependencies()
  }
  
  open fun configureDependencies() {
    AppDependenciesProvider.initialize(AppComponent.createComponent(DefaultProviders(this)))
  }
}