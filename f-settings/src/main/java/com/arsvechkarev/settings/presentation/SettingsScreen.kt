package com.arsvechkarev.settings.presentation

import android.view.Gravity
import androidx.appcompat.widget.SwitchCompat
import com.arsvechkarev.core.extenstions.i
import com.arsvechkarev.core.extenstions.screenHeight
import com.arsvechkarev.core.extenstions.screenWidth
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.core.viewbuilding.Styles.BoldTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.settings.R
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.background
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.margin
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.padding
import com.arsvechkarev.viewdsl.setColors
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.SettingsThreeElementsView
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.drawables.ProfileDrawable
import com.google.firebase.auth.FirebaseAuth

class SettingsScreen : Screen() {
  
  override fun buildLayout() = withViewBuilder {
    RootScrollableVerticalLayout {
      val username = FirebaseAuth.getInstance().currentUser!!.displayName!!
      val email = FirebaseAuth.getInstance().currentUser!!.email!!
      child<Toolbar>(MatchParent, WrapContent) {
        title(R.string.title_settings)
        onBackClick { navigator.onBackPress() }
      }
      val size = (minOf(context.screenWidth, context.screenHeight) / 2.5f).i
      ImageView(size, size) {
        margin(24.dp)
        layoutGravity(Gravity.CENTER)
        background(ProfileDrawable(context))
      }
      TextView(WrapContent, WrapContent, style = BoldTextView) {
        margins(top = 24.dp)
        layoutGravity(Gravity.CENTER)
        textSize(TextSizes.H1)
        text(username)
      }
      TextView(WrapContent, WrapContent, style = BaseTextView) {
        margins(top = 16.dp)
        layoutGravity(Gravity.CENTER)
        textSize(TextSizes.H2)
        textColor(Colors.TextSecondary)
        text(email)
      }
      FrameLayout(MatchParent, WrapContent) {
        margins(top = 24.dp)
        padding(16.dp)
        TextView(WrapContent, WrapContent, style = BoldTextView) {
          textSize(TextSizes.H3)
          layoutGravity(Gravity.START)
          text("Notifications")
        }
        child<SwitchCompat>(WrapContent, WrapContent) {
          layoutGravity(Gravity.END)
          setColors(Colors.AccentLight, Colors.Disabled, Colors.OnAccent, Colors.Surface)
        }
      }
      child<SettingsThreeElementsView>(MatchParent, WrapContent) {
        margins(top = 24.dp)
      }
    }
  }
}