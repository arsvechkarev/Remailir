package com.arsvechkarev.storage.di

import android.content.Context
import core.di.ContextModule
import storage.FileStorage
import storage.Preferences
import storage.PreferencesImpl
import storage.Storage
import dagger.Module
import dagger.Provides

@Module(includes = [ContextModule::class])
class StorageModule {
  
  @Provides
  fun providePreferences(context: Context): Preferences = PreferencesImpl(context)
  
  @Provides
  fun provideStorage(context: Context): Storage = FileStorage(context)
}