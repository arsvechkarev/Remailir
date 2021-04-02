package core.ui.navigation

import android.os.Bundle

/**
 * Options for navigating
 */
class Options(
  val clearAllOtherScreens: Boolean = false,
  val arguments: Bundle? = null,
  val removeOnExit: Boolean = false,
  val removeCurrentScreen: Boolean = false,
)