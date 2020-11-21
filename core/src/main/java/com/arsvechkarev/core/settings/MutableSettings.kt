package com.arsvechkarev.core.settings

interface MutableSettings : Settings {
  
  fun setNotificationsEnabled(enabled: Boolean)
}