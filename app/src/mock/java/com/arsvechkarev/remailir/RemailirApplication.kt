package com.arsvechkarev.remailir

import com.arsvechkarev.core.MockModeDrawerHolder
import com.arsvechkarev.core.extenstions.readAssetsFile
import com.arsvechkarev.friends.di.FriendsDi
import timber.log.Timber

class RemailirApplication : BaseRemailirApplication() {
  
  override fun onCreate() {
    super.onCreate()
    Timber.plant(Timber.DebugTree())
    MockModeDrawerHolder.setMockModeDrawer(MockModeDrawerImpl)
  }
  
  override fun configureDependencies() {
    FriendsDi.database = FakeFirebaseDatabase(
      applicationContext.readAssetsFile("fake_friends_data.json"))
  }
}