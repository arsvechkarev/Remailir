package com.arsvechkarev.core.navigation

import com.arsvechkarev.core.model.User

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun switchToMainScreen()
  
  fun popCurrentScreen(notifyBackPress: Boolean = true)
  
  fun startChatWith(user: User)
  
  fun respondToChatWith(user: User)
  
  fun goToFriendsScreen()
  
  fun goToSearchScreen()
  
  fun goToSettingsScreen()
  
  fun goToSavedMessagesScreen()
  
  fun openEmailApp()
  
  fun openLink(link: String)
  
  fun signOut()
}