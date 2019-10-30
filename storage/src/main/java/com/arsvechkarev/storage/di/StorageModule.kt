package com.arsvechkarev.storage.di

import android.content.Context
import com.arsvechkarev.core.di.ContextModule
import com.arsvechkarev.storage.FileStorage
import com.arsvechkarev.storage.Preferences
import com.arsvechkarev.storage.PreferencesImpl
import com.arsvechkarev.storage.Storage
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class StorageModule {
  
  @Provides
  fun providePreferences(context: Context): Preferences = PreferencesImpl(context)
  
  @Provides
  fun provideStorage(context: Context): Storage = FileStorage(context)
}