package com.arsvechkarev.remailir

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.System
import android.provider.Settings.System.ACCELEROMETER_ROTATION
import android.view.View
import com.arsvechkarev.chats.presentation.ChatsScreen
import com.arsvechkarev.core.BaseActivity
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.navigation.Navigator
import com.arsvechkarev.core.navigation.NavigatorView
import com.arsvechkarev.core.navigation.Options
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.friends.presentation.FriendsScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen
import com.arsvechkarev.registration.presentation.RegistrationScreen.Companion.CHECK_LINK
import com.arsvechkarev.settings.presentation.SettingsScreen
import com.arsvechkarev.viewdsl.Densities
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.withViewBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : BaseActivity(), Navigator {
  
  private val navigator get() = viewAs<NavigatorView>()
  
  private val mainActivityLayout
    get() = withViewBuilder {
      NavigatorView(context).apply {
        classNameTag()
        size(MatchParent, MatchParent)
      }
    }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Densities.init(resources)
    Colors.init(this)
    setContentView(mainActivityLayout)
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
//        GlobalScope.launch(Dispatchers.Main) {
//          try {
//
//            FirebaseAuth.getInstance().signInWithEmailAndPassword(
//              "b@gmail.com", "bbbbbbbb"
//            ).await()
//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setDisplayName("b")
//                .build()
//            FirebaseAuth.getInstance().currentUser!!
//                .updateProfile(profileUpdates)
//                .await()
//            navigator.navigate(ChatsScreen::class)
//          } catch (e: Throwable) {
//            Timber.d(e, "keke")
//          }
//        }
    if (FirebaseAuthenticator.isUserLoggedIn()) {
      navigator.navigate(ChatsScreen::class)
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
      ChatsScreen::class,
      options = Options(clearAllOtherScreens = true)
    )
  }
  
  override fun onBackPress() {
    onBackPressed()
  }
  
  override fun goToFriendsScreen() {
    navigator.navigate(FriendsScreen::class)
  }
  
  override fun goToSearchScreen() {
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