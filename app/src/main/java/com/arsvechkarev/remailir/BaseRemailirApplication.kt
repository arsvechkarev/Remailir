package com.arsvechkarev.remailir

import android.app.Application
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.firebase.database.FirebaseDatabaseImpl
import com.arsvechkarev.friends.di.FriendsDi
import com.arsvechkarev.viewdsl.ContextHolder
import com.google.firebase.database.FirebaseDatabase

open class BaseRemailirApplication : Application() {
  
  override fun onCreate() {
    super.onCreate()
    ContextHolder.init(applicationContext)
    Fonts.init(applicationContext)
    FirebaseDatabase.getInstance().setPersistenceEnabled(false)
    configureDependencies()
  }
  
  open fun configureDependencies() {
    FriendsDi.database = FirebaseDatabaseImpl(AndroidDispatchers)
  }
}