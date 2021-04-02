package com.arsvechkarev.settings.presentation

import authentication.Authenticator
import authentication.EmailSaver
import core.Dispatchers
import core.Settings
import core.ui.BasePresenter
import core.ui.navigation.Navigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SettingsPresenter(
  private val settings: Settings,
  private val authenticator: Authenticator,
  private val emailSaver: EmailSaver,
  private var navigator: Navigator?,
  dispatchers: Dispatchers
) : BasePresenter<SettingsView>(dispatchers) {
  
  fun areNotificationsEnabled(): Boolean {
    return settings.areNotificationsEnabled
  }
  
  fun setNotificationsEnabled(enabled: Boolean) {
    settings.areNotificationsEnabled = enabled
  }
  
  fun logOut() {
    coroutine {
      viewState.showSigningOut()
      withContext(dispatchers.IO) {
        emailSaver.deleteEmailSynchronously()
        authenticator.signOut()
      }
      delay(SIGN_OUT_DURATION)
      navigator?.signOut()
    }
  }
  
  fun share() {
  
  }
  
  fun openSourceCode() {
    navigator?.openLink(SOURCE_CODE_LINK)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator = null
  }
  
  companion object {
    
    const val SOURCE_CODE_LINK = "https://github.com/arsvechkarev/Remailir"
    const val SIGN_OUT_DURATION = 1500L
  }
}