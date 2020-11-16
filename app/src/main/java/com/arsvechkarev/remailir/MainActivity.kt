package com.arsvechkarev.remailir

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings.System
import android.provider.Settings.System.ACCELEROMETER_ROTATION
import android.view.View
import com.arsvechkarev.chats.presentation.ChatsScreen
import com.arsvechkarev.core.BaseActivity
import com.arsvechkarev.core.auth.FirebaseAuthenticator
import com.arsvechkarev.core.navigation.Navigator
import com.arsvechkarev.core.navigation.NavigatorView
import com.arsvechkarev.core.navigation.Options
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.registration.presentation.RegistrationScreen
import com.arsvechkarev.viewdsl.Densities
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.withViewBuilder

class MainActivity : BaseActivity(), Navigator {
  
  private val navigator get() = viewAs<NavigatorView>(Navigator)
  
  private val mainActivityLayout
    get() = withViewBuilder {
      NavigatorView(context).apply {
        fitsSystemWindows = true
        tag(Navigator)
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
    if (FirebaseAuthenticator.isUserLoggedIn()) {
      navigator.fitsSystemWindows = false
      navigator.navigate(ChatsScreen::class)
    } else {
      navigator.navigate(RegistrationScreen::class)
    }
  }
  
  override fun switchToMainScreen() {
    if (System.getInt(contentResolver, ACCELEROMETER_ROTATION, 0) == 1) {
      requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }
    navigator.fitsSystemWindows = false
    navigator.navigate(
      ChatsScreen::class,
      options = Options(clearAllOtherScreens = true)
    )
  }
  
  override fun onBackPressed() {
    if (!navigator.handleBackStack()) {
      super.onBackPressed()
    }
  }
  
  companion object {
    
    private const val Navigator = "Navigator"
  }
}