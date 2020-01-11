package core.di

import android.content.Context
import core.di.scopes.FeatureScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {
  
  @Provides
  @FeatureScope
  fun provideContext(): Context = context
}