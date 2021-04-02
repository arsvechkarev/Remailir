package com.arsvechkarev.remailir

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.System
import android.provider.Settings.System.ACCELEROMETER_ROTATION
import android.view.View
import com.arsvechkarev.chat.presentation.ChatScreen
import com.arsvechkarev.chat.presentation.ChatScreen.Companion.KEY_TYPE
import com.arsvechkarev.chat.presentation.ChatScreen.Companion.KEY_USERNAME
import com.arsvechkarev.chat.presentation.ChatScreen.Companion.TYPE_JOINED
import com.arsvechkarev.chat.presentation.ChatScreen.Companion.TYPE_REQUEST
import com.arsvechkarev.friends.presentation.FriendsScreen
import com.arsvechkarev.home.presentation.HomeScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen.Companion.CHECK_LINK
import com.arsvechkarev.search.presentation.SearchScreen
import com.arsvechkarev.settings.presentation.SettingsScreen
import core.model.User
import core.resources.ViewBuilding
import core.ui.BaseActivity
import core.ui.navigation.Navigator
import core.ui.navigation.NavigatorView
import core.ui.navigation.Options
import viewdsl.Size.Companion.MatchParent
import viewdsl.classNameTag
import viewdsl.size
import viewdsl.withViewBuilder

class MainActivity : BaseActivity(), Navigator {
  
  private val navigator get() = viewAs<NavigatorView>()
  
  private val mainActivityLayout
    get() = withViewBuilder {
      RootView(context).apply {
        size(MatchParent, MatchParent)
        child<NavigatorView>(MatchParent, MatchParent) {
          classNameTag()
        }
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ViewBuilding.initializeWithActivityContext(this)
    setContentView(mainActivityLayout)
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    navigator.navigate(HomeScreen::class)
  }
  
  override fun switchToMainScreen() {
    if (System.getInt(contentResolver, ACCELEROMETER_ROTATION, 0) == 1) {
      requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }
    navigator.navigate(
      HomeScreen::class,
      options = Options(clearAllOtherScreens = true)
    )
  }
  
  override fun popCurrentScreen(notifyBackPress: Boolean) {
    if (notifyBackPress) {
      onBackPressed()
    } else {
      navigator.handleGoBack(notifyBackPress = false)
    }
  }
  
  override fun startChatWith(user: User) {
    navigator.navigate(ChatScreen::class,
      options = Options(
        removeOnExit = true,
        arguments = Bundle().apply {
          putString(KEY_USERNAME, user.username)
          putString(KEY_TYPE, TYPE_REQUEST)
        }))
  }
  
  override fun respondToChatWith(user: User) {
    navigator.navigate(ChatScreen::class,
      options = Options(
        removeOnExit = true,
        arguments = Bundle().apply {
          putString(KEY_USERNAME, user.username)
          putString(KEY_TYPE, TYPE_JOINED)
        }))
  }
  
  override fun goToFriendsScreen() {
    navigator.navigate(FriendsScreen::class)
  }
  
  override fun goToSearchScreen() {
    navigator.navigate(SearchScreen::class)
  }
  
  override fun goToSettingsScreen() {
    navigator.navigate(SettingsScreen::class)
  }
  
  override fun goToSavedMessagesScreen() {
  }
  
  override fun openEmailApp() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.addCategory(Intent.CATEGORY_APP_EMAIL)
    startActivity(intent)
  }
  
  override fun openLink(link: String) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(link)
    startActivity(intent)
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun signOut() {
    navigator.navigate(
      RegistrationScreen::class,
      options = Options(
        clearAllOtherScreens = true,
        arguments = Bundle().apply {
          putBoolean(CHECK_LINK, false)
        }
      )
    )
  }
}