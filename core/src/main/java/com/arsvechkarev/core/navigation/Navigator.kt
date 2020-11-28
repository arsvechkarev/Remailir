package com.arsvechkarev.core.navigation

/**
 * Host activity with necessary methods to be accessed from fragment
 */
interface Navigator {
  
  fun switchToMainScreen()
  
  fun onBackPress()
  
  fun goToFriendsScreen()
  
  fun goToSearchScreen(removeCurrentScreen: Boolean)
  
  fun goToSettingsScreen()
  
  fun goToSavedMessagesScreen()
  
  fun openEmailApp()
  
  fun openLink(link: String)
  
  fun signOut()
}