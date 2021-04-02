package core.impl

import core.KeyValueStorage
import core.Settings

class AndroidSettings(private val storage: KeyValueStorage) : Settings {
  
  override var areNotificationsEnabled: Boolean
    get() = storage.getBoolean(NOTIFICATIONS_KEY, defaultValue = true)
    set(value) = storage.execute { putBoolean(NOTIFICATIONS_KEY, value) }
  
  companion object {
    
    const val SETTINGS_FILENAME = "Settings"
    const val NOTIFICATIONS_KEY = "notificationsEnabled"
  }
}