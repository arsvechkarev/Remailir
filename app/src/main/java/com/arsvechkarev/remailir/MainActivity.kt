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
import com.arsvechkarev.core.BaseActivity
import com.arsvechkarev.core.model.User
import com.arsvechkarev.core.navigation.Navigator
import com.arsvechkarev.core.navigation.NavigatorView
import com.arsvechkarev.core.navigation.Options
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.friends.presentation.FriendsScreen
import com.arsvechkarev.home.presentation.HomeScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen.Companion.CHECK_LINK
import com.arsvechkarev.search.presentation.SearchScreen
import com.arsvechkarev.settings.presentation.SettingsScreen
import com.arsvechkarev.viewdsl.Densities
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.withViewBuilder
import com.arsvechkarev.views.RootView

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
    Densities.init(resources)
    Colors.init(this)
    setContentView(mainActivityLayout)
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    //        FirebaseFirestore.getInstance().setFirestoreSettings(
    //          FirebaseFirestoreSettings.Builder().setPersistenceEnabled(false).build())
    //            GlobalScope.launch(Dispatchers.Main) {
    //              try {
    //                FirebaseAuth.getInstance().signInWithEmailAndPassword(
    //                  "a@gmail.com", "aaaaaaaa"
    //                ).await()
    //                val profileUpdates = UserProfileChangeRequest.Builder()
    //                    .setDisplayName("a")
    //                    .build()
    //                FirebaseAuth.getInstance().currentUser!!
    //                    .updateProfile(profileUpdates)
    //                    .await()
    //                navigator.navigate(HomeScreen::class)
    //              } catch (e: Throwable) {
    //                Timber.d(e, "Failed to sign in")
    //              }
    //            }
    if (FirebaseAuthenticator.isUserLoggedIn()) {
      navigator.navigate(HomeScreen::class)
    } else {
      navigator.navigate(RegistrationScreen::class,
        options = Options(
          clearAllOtherScreens = true,
          arguments = Bundle().apply { putBoolean(CHECK_LINK, true) }
        )
      )
    }
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