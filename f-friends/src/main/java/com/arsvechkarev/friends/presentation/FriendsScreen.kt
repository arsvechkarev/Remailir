package com.arsvechkarev.friends.presentation

import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.friends.R
import com.arsvechkarev.viewdsl.Size
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.font
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.Toolbar

class FriendsScreen : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout {
      backgroundColor(Colors.Surface)
      child<Toolbar>(Size.MatchParent, Size.WrapContent) {
        showBackImage = false
        title.text(R.string.title_friends)
      }
      child<TextView>(Size.MatchParent, Size.WrapContent) {
        layoutGravity(Gravity.CENTER)
        onClick { navigator.goToFriendsScreen2() }
        gravity(Gravity.CENTER)
        text("Friends shown here")
        textSize(TextSizes.H0)
        textColor(Colors.TextPrimary)
        font(Fonts.SegoeUiBold)
      }
    }
  }
}

class FriendsScreen2 : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout {
      backgroundColor(Colors.AccentLight)
      child<Toolbar>(Size.MatchParent, Size.WrapContent) {
        showBackImage = false
        title.text(R.string.title_friends)
      }
      child<TextView>(Size.MatchParent, Size.WrapContent) {
        layoutGravity(Gravity.CENTER)
        onClick { navigator.goToFriendsScreen3() }
        gravity(Gravity.CENTER)
        text("Friends 2")
        textSize(TextSizes.H0)
        textColor(Colors.TextPrimary)
        font(Fonts.SegoeUiBold)
      }
    }
  }
}

class FriendsScreen3 : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout {
      backgroundColor(Colors.OnAccent)
      child<Toolbar>(Size.MatchParent, Size.WrapContent) {
        showBackImage = false
        title.text(R.string.title_friends)
      }
      child<TextView>(Size.MatchParent, Size.WrapContent) {
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        text("Friends 3")
        textSize(TextSizes.H0)
        textColor(Colors.TextPrimary)
        font(Fonts.SegoeUiBold)
      }
    }
  }
}