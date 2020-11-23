package com.arsvechkarev.chats.presentation

import android.annotation.SuppressLint
import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.chats.R
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.classNameTag
import com.arsvechkarev.viewdsl.font
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.menu.MenuView
import com.google.firebase.auth.FirebaseAuth

class ChatsScreen : Screen() {
  
  @SuppressLint("RtlHardcoded")
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout(MatchParent, MatchParent) {
      clipChildren = false
      child<Toolbar>(MatchParent, WrapContent) {
        showBackImage = false
        title.text(R.string.title_chats)
      }
      child<TextView>(MatchParent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        val email = FirebaseAuth.getInstance().currentUser!!.email
        val username = FirebaseAuth.getInstance().currentUser!!.displayName
        text("$email\n$username")
        textSize(TextSizes.H0)
        textColor(Colors.TextPrimary)
        font(Fonts.SegoeUiBold)
      }
      child<MenuView>(WrapContent, WrapContent) {
        classNameTag()
        layoutGravity(Gravity.BOTTOM or Gravity.RIGHT)
        firstMenuItem.onClick { closeMenu(); navigator.goToFriendsScreen() }
        secondMenuItem.onClick { closeMenu(); navigator.goToSearchScreen() }
        thirdMenuItem.onClick { closeMenu(); navigator.goToSettingsScreen() }
        fourthMenuItem.onClick { closeMenu(); navigator.goToSavedMessagesScreen() }
      }
    }
  }
  
  override fun onBackPressed(): Boolean {
    val menuView = viewAs<MenuView>()
    if (menuView.isOpened) {
      menuView.closeMenu()
      return true
    }
    return false
  }
}