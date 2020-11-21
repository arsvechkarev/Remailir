package com.arsvechkarev.core.navigation

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.WindowInsets
import android.widget.FrameLayout
import com.arsvechkarev.core.extenstions.ifNotNull
import com.arsvechkarev.viewdsl.DURATION_SHORT
import com.arsvechkarev.viewdsl.animateGone
import com.arsvechkarev.viewdsl.animateSlideFromRight
import com.arsvechkarev.viewdsl.animateSlideToRight
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.invisible
import kotlin.reflect.KClass


class NavigatorView(context: Context) : FrameLayout(context) {
  
  private val screenClassesToScreens = HashMap<String, Screen>()
  private val screens = ArrayList<Screen>()
  private var _currentScreen: Screen? = null
  
  val currentScreen get() = _currentScreen!!
  
  init {
    fitsSystemWindows = true
  }
  
  /**
   * Navigates to screen [screenClass] with given [options]
   */
  fun navigate(screenClass: KClass<out Screen>, options: Options = Options()) {
    if (_currentScreen != null && _currentScreen!!::class.java.name == screenClass.java.name) {
      return
    }
    if (options.clearAllOtherScreens) {
      screenClassesToScreens.values.forEach { releaseScreen(it) }
      screenClassesToScreens.clear()
      screens.clear()
      removeAllViews()
    }
    val screen = screenClassesToScreens[screenClass.java.name] ?: run {
      val constructor = screenClass.java.getConstructor()
      val instance = constructor.newInstance()
      val className = instance::class.java.name
      screenClassesToScreens[className] = instance
      instance.metadata._context = context
      instance.metadata._arguments = options.arguments
      instance.metadata.removeOnExit = options.removeOnExit
      instance
    }
    val view = screen.view ?: run {
      val builtView = screen.buildLayout()
      screen.metadata._view = builtView
      builtView.tag = screen::class.java.name
      builtView
    }
    view.invisible() // Make view invisible so that we can animate it later
    _currentScreen.ifNotNull { hideScreen(it, animateSlideToRight = false) }
    screens.add(screen)
    if (view.isAttachedToWindow) {
      showScreen(screen, animateSlideFromRight = true)
    } else {
      addView(view, MATCH_PARENT, MATCH_PARENT)
      screen.onInitDelegate()
      if (screen.metadata._arguments == null) {
        screen.onInit()
      } else {
        screen.onInit(screen.metadata._arguments!!)
      }
      showScreen(screen, animateSlideFromRight = true)
    }
    println("kkk nav = ${screens.size}, $childCount")
    _currentScreen = screen
  }
  
  /**
   * Returns true if there are screens in the stack or screen handled back press
   */
  fun handleGoBack(): Boolean {
    if (screens.isEmpty()) return false
    val lastScreen = screens.last()
    if (lastScreen.onBackPressed()) {
      return true
    }
    if (screens.size == 1) {
      releaseScreen(_currentScreen!!)
      screenClassesToScreens.clear()
      screens.clear()
      _currentScreen = null
      return false
    }
    hideScreen(lastScreen, animateSlideToRight = true)
    if (lastScreen.metadata.removeOnExit) {
      lastScreen.view.ifNotNull { view ->
        view.apply(screenHidingAnimation)
        postDelayed({ performRemoveView(view) }, (ANIMATION_DURATION * 1.2f).toLong())
      }
    }
    screens.removeLast()
    _currentScreen = screens.last()
    showScreen(_currentScreen!!, animateSlideFromRight = false)
    println("kkk rm = ${screens.size}, $childCount")
    return true
  }
  
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    _currentScreen.ifNotNull { checkForOrientation(it, newConfig) }
  }
  
  override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
      insets.systemWindowInsetBottom))
  }
  
  private fun checkForOrientation(screen: Screen, config: Configuration) {
    if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
      screen.onOrientationBecamePortrait()
    } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      screen.onOrientationBecameLandscape()
    }
  }
  
  private fun showScreen(screen: Screen, animateSlideFromRight: Boolean) {
    if (animateSlideFromRight) {
      screen.view?.apply(newScreenAppearanceAnimation)
    } else {
      screen.view?.apply(screenReappearanceAnimation)
    }
    screen.onAppearedOnScreenDelegate()
    screen.onAppearedOnScreen()
    checkForOrientation(screen, context.resources.configuration)
  }
  
  private fun hideScreen(screen: Screen, animateSlideToRight: Boolean) {
    screen.onRelease()
    screen.onDetachDelegate()
    if (animateSlideToRight) {
      screen.view?.apply(screenHidingAnimation)
    } else {
      screen.view?.apply(oldScreenHidingAnimation)
    }
  }
  
  private fun performRemoveView(child: View) {
    removeView(child)
    val screen = screenClassesToScreens.getValue(child.tag as String)
    releaseScreen(screen)
    screens.remove(screen)
    screenClassesToScreens.remove(screen.javaClass.name)
  }
  
  private fun releaseScreen(screen: Screen) {
    screen.onRelease()
    screen.metadata._context = null
    screen.metadata._view = null
    screen.onDestroyDelegate()
  }
  
  companion object {
    
    private const val ANIMATION_DURATION = DURATION_SHORT
    
    private val newScreenAppearanceAnimation: View.() -> Unit = {
      animateSlideFromRight(duration = ANIMATION_DURATION)
    }
    
    private val screenReappearanceAnimation: View.() -> Unit = {
      animateVisible(duration = ANIMATION_DURATION)
    }
    
    private val screenHidingAnimation: View.() -> Unit = {
      animateSlideToRight(duration = ANIMATION_DURATION)
    }
    
    private val oldScreenHidingAnimation: View.() -> Unit = {
      animateGone(duration = ANIMATION_DURATION)
    }
  }
}