package navigation

import com.github.terrakok.cicerone.Command

data class Forward(val screenInfo: ScreenInfo, val clearContainer: Boolean = false) : Command

data class Replace(val screenInfo: ScreenInfo) : Command

data class Back(val releaseCurrentScreen: Boolean = true) : Command

data class BackTo(val screenInfo: ScreenInfo, val releaseAllLeftScreens: Boolean = true) : Command

data class SwitchToNewRoot(val screenInfo: ScreenInfo) : Command