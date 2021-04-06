package navigation.screens

import navigation.ScreenInfo


/**
 * Screens for application
 */
interface Screens {
  
  val Registration: ScreenInfo
  val Home: ScreenInfo
  val Chat: ScreenInfo
  val Friends: ScreenInfo
  val Search: ScreenInfo
  val Settings: ScreenInfo
  
  companion object {
    
    private var screensImpl: Screens? = null
    
    fun setImplementation(screens: Screens) {
      screensImpl = screens
    }
    
    /**
     * Allows for using interface like 'Screens().MyScreen1', 'Screens().MyScreen2', etc
     */
    operator fun invoke(): Screens = screensImpl ?: error(
      "You should call setImplementation(Screens) first")
  }
}