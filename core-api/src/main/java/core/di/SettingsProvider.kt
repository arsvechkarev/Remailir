package core.di

import core.Settings

interface SettingsProvider {
  
  fun provideSettings(): Settings
}