package com.arsvechkarev.core.navigation

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun switchToMainScreen()
  
  fun openEmailApp()
  
  fun goToFriendsScreen()
  fun goToFriendsScreen2()
  fun goToFriendsScreen3()
  
  fun goToSearchScreen()
  
  fun goToSettingsScreen()
  
  fun goToSavedMessagesScreen()
}