package com.arsvechkarev.remailir

import android.os.Bundle
import android.view.View
import com.arsvechkarev.remailir.di.NavigationComponent
import com.arsvechkarev.remailir.navigation.ScreensImplementation
import com.github.terrakok.cicerone.Cicerone
import core.di.AuthDependenciesProvider
import core.resources.ViewBuilding
import core.ui.BaseActivity
import navigation.ExtendedNavigator
import navigation.Router
import navigation.screens.Screens
import viewdsl.Size.Companion.MatchParent
import viewdsl.id
import viewdsl.size
import viewdsl.withViewBuilder
import javax.inject.Inject
import navigation.RouterProvider.Companion.instance as routerProvider

class MainActivity : BaseActivity() {
  
  private val mainActivityLayout
    get() = withViewBuilder {
      RootView(context).apply {
        id(rootViewId)
        size(MatchParent, MatchParent)
      }
    }
  
  @Inject
  lateinit var navigator: ExtendedNavigator
  
  @Inject
  lateinit var cicerone: Cicerone<Router>
  
  @Inject
  lateinit var router: Router
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    ViewBuilding.initializeWithActivityContext(this)
    setContentView(mainActivityLayout)
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    Screens.setImplementation(ScreensImplementation)
    NavigationComponent.createComponent(this, rootViewId, routerProvider).inject(this)
    figureOutScreenToGo(savedInstanceState)
  }
  
  private fun figureOutScreenToGo(savedInstanceState: Bundle?) {
    if (savedInstanceState != null) {
      // Activity is recreated, navigator handles this case automatically
      return
    }
    if (AuthDependenciesProvider.instance.provideAuthenticator().isUserLoggedIn()) {
      router.goForward(Screens().Home)
    } else {
      router.goForward(Screens().Registration)
    }
  }
  
  override fun onResume() {
    super.onResume()
    cicerone.getNavigatorHolder().setNavigator(navigator)
  }
  
  override fun onPause() {
    super.onPause()
    cicerone.getNavigatorHolder().removeNavigator()
  }
  
  override fun onBackPressed() {
    if (!navigator.handleGoBack()) {
      super.onBackPressed()
    }
  }
  
  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    navigator.onSaveInstanceState(outState)
  }
  
  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    navigator.onRestoreInstanceState(savedInstanceState)
  }
  
  override fun onDestroy() {
    super.onDestroy()
    navigator.releaseScreens()
  }
  
  companion object {
    
    private val rootViewId = View.generateViewId()
  }
}