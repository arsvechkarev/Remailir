package com.arsvechkarev.remailir.navigation

import com.arsvechkarev.chat.presentation.ChatScreen
import com.arsvechkarev.friends.presentation.FriendsScreen
import com.arsvechkarev.home.presentation.HomeScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen
import com.arsvechkarev.search.presentation.SearchScreen
import com.arsvechkarev.settings.presentation.SettingsScreen
import navigation.Screen
import navigation.screens.Screens

object ScreensImplementation : Screens {
  
  override val Registration = Screen { RegistrationScreen::class }
  override val Home = Screen { HomeScreen::class }
  override val Chat = Screen { ChatScreen::class }
  override val Friends = Screen { FriendsScreen::class }
  override val Search = Screen { SearchScreen::class }
  override val Settings = Screen { SettingsScreen::class }
}