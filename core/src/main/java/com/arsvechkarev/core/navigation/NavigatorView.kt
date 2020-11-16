package com.arsvechkarev.core.navigation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.arsvechkarev.core.extenstions.ifNotNull
import com.arsvechkarev.viewdsl.gone
import com.arsvechkarev.viewdsl.visible
import kotlin.reflect.KClass

class NavigatorView(context: Context) : FrameLayout(context) {
  
  private val screenClassesToScreens = LinkedHashMap<String, Screen>()
  private val screens = ArrayList<Screen>()
  private var _currentScreen: Screen? = null
  
  val currentScreen: Screen? get() = _currentScreen
  
  fun navigate(
    screenClass: KClass<out Screen>,
    arguments: Bundle? = null,
    options: Options = Options()
  ) {
    if (_currentScreen != null && _currentScreen!!::class == screenClass) {
      return
    }
    if (options.clearAllOtherScreens) {
      removeAllViews()
    }
    val screen = screenClassesToScreens[screenClass::class.java.name] ?: run {
      val constructor = screenClass.java.getConstructor()
      val instance = constructor.newInstance()
      val className = instance::class.java.name
      screenClassesToScreens[className] = instance
      instance._context = context
      instance._arguments = arguments
      instance
    }
    val view = screen.view ?: run {
      val builtView = screen.buildLayout()
      screen._view = builtView
      builtView.tag = screen::class.java.name
      builtView
    }
    if (view.isAttachedToWindow) {
      _currentScreen.ifNotNull { hideScreen(it) }
      _currentScreen?.view?.gone()
      showScreen(screen)
      _currentScreen = screen
    } else {
      addView(view, MATCH_PARENT, MATCH_PARENT)
      _currentScreen = screen
      screens.add(screen)
    }
  }
  
  fun handleBackStack(): Boolean {
    if (screens.isEmpty()) return false
    val last = screens.last()
    if (screens.size == 1) {
      _currentScreen = null
      return false
    }
    hideScreen(last)
    _currentScreen = screens[screens.size - 2]
    _currentScreen!!.viewNonNull.visibility = View.VISIBLE
    return true
  }
  
  override fun onViewAdded(child: View) {
    val screen = screenClassesToScreens.getValue(child.tag as String)
    screen.onInitDelegate()
    if (screen._arguments == null) {
      screen.onInit()
    } else {
      screen.onInit(screen._arguments!!)
    }
    showScreen(screen)
  }
  
  override fun onViewRemoved(child: View) {
    val screen = screenClassesToScreens.getValue(child.tag as String)
    releaseScreen(screen)
  }
  
  override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    val values = screenClassesToScreens.values
    for (v in values) {
      releaseScreen(v)
    }
    screenClassesToScreens.clear()
  }
  
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    _currentScreen.ifNotNull { checkForOrientation(it, newConfig) }
  }
  
  private fun checkForOrientation(screen: Screen, config: Configuration) {
    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
      screen.onOrientationBecamePortrait()
    } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      screen.onOrientationBecameLandscape()
    }
  }
  
  private fun showScreen(screen: Screen) {
    screen.viewNonNull.visible()
    screen.onAppearedOnScreenDelegate()
    screen.onAppearedOnScreen()
    checkForOrientation(screen, context.resources.configuration)
  }
  
  private fun hideScreen(screen: Screen) {
    screen.onRelease()
    screen.onReleaseDelegate()
    screen.viewNonNull.visibility = View.GONE
  }
  
  private fun releaseScreen(screen: Screen) {
    screen.onRelease()
    screen._context = null
    screen._view = null
    screen.onDestroyDelegate()
    screens.remove(screen)
    screenClassesToScreens.remove(screen::class.java.name)
  }
}