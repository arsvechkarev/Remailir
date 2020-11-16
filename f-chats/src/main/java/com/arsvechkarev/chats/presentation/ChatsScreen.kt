package com.arsvechkarev.chats.presentation

import android.view.Gravity
import android.widget.TextView
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.font
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize

class ChatsScreen : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    FrameLayout(MatchParent, MatchParent) {
      child<TextView>(MatchParent, WrapContent) {
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        text("Chats are there")
        textSize(TextSizes.H0)
        textColor(Colors.TextPrimary)
        font(Fonts.SegoeUiBold)
      }
    }
  }
}