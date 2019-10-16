package com.arsvechkarev.core.di

import android.content.Context
import com.arsvechkarev.core.di.FeatureScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ContextModule(private val context: Context) {
  
  @Provides
  @FeatureScope
  fun provideContext(): Context = context
}