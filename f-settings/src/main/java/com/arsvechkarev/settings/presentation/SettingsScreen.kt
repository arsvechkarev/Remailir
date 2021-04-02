package com.arsvechkarev.settings.presentation

import android.view.Gravity
import androidx.appcompat.widget.SwitchCompat
import com.arsvechkarev.settings.R
import com.arsvechkarev.views.MaterialProgressBar
import com.arsvechkarev.views.MaterialProgressBar.Thickness.THICK
import com.arsvechkarev.views.SettingsThreeElementsView
import com.arsvechkarev.views.SimpleDialog
import com.arsvechkarev.views.Toolbar
import com.arsvechkarev.views.drawables.ProfileDrawable
import com.google.firebase.auth.FirebaseAuth
import core.resources.Colors
import core.resources.Dimens.ProgressBarSize
import core.resources.Styles.BaseTextView
import core.resources.Styles.BoldTextView
import core.resources.TextSizes
import core.ui.navigation.Screen
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.addView
import viewdsl.background
import viewdsl.backgroundRoundRect
import viewdsl.classNameTag
import viewdsl.gravity
import viewdsl.layoutGravity
import viewdsl.margin
import viewdsl.margins
import viewdsl.padding
import viewdsl.paddingHorizontal
import viewdsl.screenHeight
import viewdsl.screenWidth
import viewdsl.setColors
import viewdsl.size
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize

class SettingsScreen : Screen(), SettingsView {
  
  override fun buildLayout() = withViewBuilder {
    RootFrameLayout {
      ScrollableVerticalLayout {
        val username = FirebaseAuth.getInstance().currentUser!!.displayName!!
        val email = FirebaseAuth.getInstance().currentUser!!.email!!
        child<Toolbar>(MatchParent, WrapContent) {
          title(R.string.title_settings)
          onBackClick { navigator.popCurrentScreen() }
        }
        val size = (minOf(context.screenWidth, context.screenHeight) / 2.5f).toInt()
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
            text(R.string.text_notifications)
          }
          child<SwitchCompat>(WrapContent, WrapContent) {
            classNameTag()
            layoutGravity(Gravity.END)
            setColors(
              Colors.AccentLight, Colors.Disabled,
              Colors.OnAccent, Colors.Surface)
            setOnCheckedChangeListener { _, isChecked ->
              //              presenter.setNotificationsEnabled(isChecked)
            }
          }
        }
        child<SettingsThreeElementsView>(MatchParent, WrapContent) {
          margins(top = 24.dp)
          //          onLogOutClick { presenter.logOut() }
          //          onShareClick { presenter.share() }
          //          onSourceCodeClick { presenter.openSourceCode() }
        }
      }
      child<SimpleDialog>(MatchParent, MatchParent) {
        classNameTag()
        VerticalLayout(WrapContent, WrapContent) {
          padding(16.dp)
          gravity(Gravity.CENTER_HORIZONTAL)
          backgroundRoundRect(8.dp, Colors.Dialog)
          TextView(WrapContent, WrapContent, style = BoldTextView) {
            paddingHorizontal(24.dp)
            margins(bottom = 32.dp)
            text("Logging out")
            textSize(TextSizes.H3)
          }
          addView {
            MaterialProgressBar(context, Colors.AccentLight, THICK).apply {
              size(ProgressBarSize, ProgressBarSize)
            }
          }
        }
      }
    }
  }
  
  //  private val presenter by moxyPresenter {
  //    val storage = core.impl.SharedPrefsStorage(SETTINGS_FILENAME, contextNonNull)
  //    SettingsPresenter(
  //      core.impl.AndroidSettings(storage),
  //      core.impl.firebase.FirebaseAuthenticator,
  //      core.impl.firebase.SharedPrefsEmailSaver(contextNonNull),
  //      navigator,
  //      core.impl.AndroidDispatchers
  //    )
  //  }
  //
  override fun onInit() {
    //    viewAs<SwitchCompat>().isChecked = presenter.areNotificationsEnabled()
  }
  
  override fun showSigningOut() {
    viewAs<SimpleDialog>().show()
  }
}