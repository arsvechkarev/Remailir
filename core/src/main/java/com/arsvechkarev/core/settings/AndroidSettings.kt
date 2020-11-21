package com.arsvechkarev.core.settings

import com.arsvechkarev.core.KeyValueStorage

class AndroidSettings(
  private val storage: KeyValueStorage
) : MutableSettings {
  
  override val areNotificationsEnabled: Boolean
    get() = storage.getBoolean(NOTIFICATIONS_KEY, defaultValue = true)
  
  override fun setNotificationsEnabled(enabled: Boolean) {
    storage.execute { putBoolean(NOTIFICATIONS_KEY, enabled) }
  }
  
  companion object {
    
    const val SETTINGS_FILENAME = "Settings"
    const val NOTIFICATIONS_KEY = "notificationsEnabled"
  }
}