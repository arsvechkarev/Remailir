package com.arsvechkarev.registration.presentation

import android.content.Intent
import android.view.Gravity
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.arsvechkarev.core.extenstions.getRegistrationMessageRes
import com.arsvechkarev.core.extenstions.ifNotNull
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.core.viewbuilding.Styles
import com.arsvechkarev.core.viewbuilding.Styles.ClickableButton
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.registration.R
import com.arsvechkarev.registration.di.RegistrationInjector
import com.arsvechkarev.registration.presentation.RegistrationDimens.EditTextPadding
import com.arsvechkarev.registration.presentation.RegistrationDimens.MarginBottom
import com.arsvechkarev.registration.presentation.RegistrationDimens.MarginHorizontal
import com.arsvechkarev.registration.presentation.RegistrationDimens.MarginHorizontalBig
import com.arsvechkarev.registration.presentation.RegistrationDimens.MarginTop
import com.arsvechkarev.registration.presentation.RegistrationDimens.MarginTopSmall
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.Size.Companion.MatchParent
import com.arsvechkarev.viewdsl.Size.Companion.WrapContent
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.backgroundColor
import com.arsvechkarev.viewdsl.font
import com.arsvechkarev.viewdsl.gravity
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.layoutGravity
import com.arsvechkarev.viewdsl.margins
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.orientation
import com.arsvechkarev.viewdsl.padding
import com.arsvechkarev.viewdsl.paddings
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.tag
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.views.CheckmarkView
import com.arsvechkarev.views.ProgressBar
import com.arsvechkarev.views.SingInButton
import timber.log.Timber

class RegistrationScreen : Screen(), RegistrationView {
  
  @Suppress("RemoveExplicitTypeArguments")
  override fun buildLayout() = withViewBuilder {
    FrameLayout(MatchParent, MatchParent) {
      backgroundColor(Colors.Background)
      child<LinearLayout>(MatchParent, WrapContent) {
        tag(LayoutMain)
        layoutGravity(Gravity.CENTER)
        orientation(LinearLayout.VERTICAL)
        child<TextView>(WrapContent, WrapContent) {
          layoutGravity(Gravity.CENTER)
          margins(top = MarginTop)
          textSize(TextSizes.Header)
          text(R.string.title_log_in)
          font(Fonts.SegoeUiBold)
        }
        child<TextView>(MatchParent, WrapContent) {
          gravity(Gravity.CENTER)
          margins(start = MarginHorizontal, end = MarginHorizontal, top = MarginTop)
          textSize(TextSizes.H4)
          text(R.string.text_enter_your_email)
          font(Fonts.SegoeUi)
        }
        child<EditText>(MatchParent, WrapContent) {
          tag(EditTextEmail)
          margins(start = MarginHorizontal, end = MarginHorizontal, top = MarginTop)
          font(Fonts.SegoeUi)
          textSize(TextSizes.H3)
          padding(EditTextPadding)
          maxLines = 1
          isEnabled = false
          setHint(R.string.hint_edit_text_email)
        }
        child<TextView>(WrapContent, WrapContent) {
          tag(TextEmailError)
          invisible()
          gravity(Gravity.CENTER)
          margins(start = MarginHorizontalBig, end = MarginHorizontalBig, top = MarginTop)
          textColor(Colors.Error)
          textSize(TextSizes.H4)
          font(Fonts.SegoeUi)
        }
        child<TextView>(MatchParent, WrapContent) {
          tag(TextLinkWasSent)
          invisible()
          gravity(Gravity.CENTER)
          margins(start = MarginHorizontal, end = MarginHorizontal)
          textSize(TextSizes.H3)
          text(R.string.error_email_sent)
          font(Fonts.SegoeUi)
        }
        child<TextView>(MatchParent, WrapContent) {
          tag(TextTimer)
          invisible()
          textColor(Colors.TextSecondary)
          gravity(Gravity.CENTER)
          margins(top = MarginTopSmall, start = MarginHorizontal, end = MarginHorizontal)
          textSize(TextSizes.H4)
          font(Fonts.SegoeUi)
        }
        child<TextView>(WrapContent, WrapContent, style = ClickableButton()) {
          tag(TextOpenEmailApp)
          invisible()
          layoutGravity(Gravity.CENTER)
          text(R.string.text_open_email_app)
          margins(top = MarginTop, start = MarginHorizontal, end = MarginHorizontal)
          onClick {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_APP_EMAIL)
            contextNonNull.startActivity(intent)
          }
        }
      }
      child<LinearLayout>(MatchParent, WrapContent) {
        tag(LayoutLoading)
        invisible()
        orientation(LinearLayout.VERTICAL)
        layoutGravity(Gravity.CENTER)
        gravity(Gravity.CENTER)
        child<TextView>(WrapContent, WrapContent, style = Styles.BoldTextView) {
          tag(TextVerifyingLink)
          text(R.string.text_verifying_link)
          padding(24.dp)
          textSize(TextSizes.H1)
        }
        child<FrameLayout>(WrapContent, WrapContent) {
          child<CheckmarkView>(Dimens.CheckmarkWidth, Dimens.CheckmarkHeight) {
            invisible()
            tag(Checkmark)
          }
          addView(ProgressBar(context, Colors.Accent, ProgressBar.Thickness.THICK).apply {
            tag(ProgressBarTag)
            size(Dimens.ProgressBarSizeBig, Dimens.ProgressBarSizeBig)
          })
        }
      }
      child<LinearLayout>(MatchParent, MatchParent) {
        tag(LayoutError)
        invisible()
        gravity(Gravity.CENTER)
        layoutGravity(Gravity.CENTER)
        orientation(LinearLayout.VERTICAL)
        child<ImageView>(Dimens.ErrorLayoutImageSize, Dimens.ErrorLayoutImageSize) {
          image(R.drawable.image_unknown_error)
          margins(bottom = Dimens.ErrorLayoutTextPadding)
        }
        child<TextView>(WrapContent, WrapContent, style = Styles.BoldTextView) {
          tag(TextError)
          gravity(Gravity.CENTER)
          paddings(
            start = Dimens.ErrorLayoutTextPadding,
            end = Dimens.ErrorLayoutTextPadding,
            bottom = Dimens.ErrorLayoutTextPadding
          )
          textSize(TextSizes.H2)
          text(R.string.error_email_link_expired)
        }
        child<TextView>(WrapContent, WrapContent, style = ClickableButton(
          colorStart = Colors.ErrorGradientStart,
          colorEnd = Colors.ErrorGradientEnd,
        )) {
          tag(ButtonRetry)
        }
      }
      child<SingInButton>(MatchParent, WrapContent) {
        tag(ButtonSignIn)
        margins(start = MarginHorizontal, end = MarginHorizontal, bottom = MarginBottom)
        layoutGravity(Gravity.BOTTOM)
        onClick {
          val email = editText(EditTextEmail).text.toString().trim()
          presenter.sendEmailLink(email)
        }
      }
    }
  }
  
  private val presenter by moxyPresenter { RegistrationInjector.providePresenter(this) }
  
  override fun onInit() {
    presenter.figureOutScreenToGo(activityNonNull.intent)
  }
  
  override fun showInitialState() {
    view(EditTextEmail).isEnabled = true
    view(EditTextEmail).requestFocus()
  }
  
  override fun animateToInitialState() {
    view(LayoutError).animateInvisible()
    view(ButtonSignIn).animateVisible()
    view(LayoutMain).animateVisible()
    view(EditTextEmail).isEnabled = true
    view(EditTextEmail).requestFocus()
  }
  
  override fun showLoading() {
    view(LayoutError).animateInvisible()
    view(TextEmailError).animateInvisible()
    viewAs<SingInButton>(ButtonSignIn).showProgress()
    view(LayoutMain).animateVisible()
    view(ButtonSignIn).animateVisible()
    view(ButtonSignIn).isClickable = false
  }
  
  override fun showVerifyingLink() {
    view(ButtonSignIn).invisible()
    view(LayoutMain).invisible()
    view(LayoutError).invisible()
    view(LayoutLoading).animateVisible()
  }
  
  override fun showSuccessFullyVerified() {
    view(ProgressBarTag).animateInvisible(andThen = {
      textView(TextVerifyingLink).text(R.string.text_successfully_verified)
      viewAs<CheckmarkView>(Checkmark).animateCheckmark(andThen = {
        view(LayoutLoading).animateInvisible(andThen = {
          presenter.goToMainFragment()
        })
      })
    })
  }
  
  override fun showEmailIsCorrect() {
    view(TextEmailError).animateInvisible()
  }
  
  override fun showEmailIsIncorrect(messageResId: Int) {
    textView(TextEmailError).text(messageResId)
    textView(TextEmailError).animateVisible()
  }
  
  override fun showNoEmailSaved() {
    showFailureLayout(
      R.string.error_while_checking_link,
      R.string.text_retry,
      onClickAction = { presenter.animateToInitialState() }
    )
  }
  
  override fun showVerificationLinkExpired(email: String) {
    showFailureLayout(
      R.string.error_email_link_expired,
      R.string.text_resend_link,
      onClickAction = { presenter.sendEmailLink(email) }
    )
  }
  
  override fun showEmailVerificationFailure(e: Throwable) {
  }
  
  override fun showFailure(e: Throwable) {
    Timber.d(e, "Registration error")
    view(ButtonSignIn).isClickable = true
    textView(TextEmailError).text(e.getRegistrationMessageRes())
    textView(TextEmailError).animateVisible()
    viewAs<SingInButton>(ButtonSignIn).hideProgress()
  }
  
  override fun showTimeTicking(time: CharSequence) {
    textView(TextTimer).text(getString(R.string.number_resend_link, time))
  }
  
  override fun showTimeHasRunOut() {
    view(TextTimer).animateInvisible()
    view(TextOpenEmailApp).animateInvisible()
    view(TextLinkWasSent).animateInvisible()
    view(ButtonSignIn).isEnabled = true
    view(EditTextEmail).isEnabled = true
    view(ButtonSignIn).isClickable = true
  }
  
  override fun showEmailSent(email: String?) {
    email.ifNotNull { editText(EditTextEmail).text(it) }
    viewAs<SingInButton>(ButtonSignIn).hideProgress()
    view(TextLinkWasSent).animateVisible()
    view(TextOpenEmailApp).animateVisible()
    view(TextTimer).animateVisible()
    view(ButtonSignIn).isEnabled = false
    view(EditTextEmail).isEnabled = false
  }
  
  private fun showFailureLayout(
    textErrorRes: Int,
    textButtonRetryRes: Int,
    onClickAction: () -> Unit
  ) {
    view(LayoutMain).animateInvisible()
    view(ButtonSignIn).animateInvisible()
    view(LayoutLoading).animateInvisible()
    view(LayoutError).animateVisible()
    textView(TextError).text(textErrorRes)
    textView(ButtonRetry).text(textButtonRetryRes)
    textView(ButtonRetry).onClick(onClickAction)
  }
  
  companion object {
    
    private const val EditTextEmail = "EditTextEmail"
    private const val ButtonSignIn = "ButtonSignIn"
    private const val TextTimer = "TextTimer"
    private const val TextOpenEmailApp = "TextOpenEmailApp"
    private const val TextEmailError = "TextEmailError"
    private const val TextLinkWasSent = "TextLinkWasSent"
    
    private const val LayoutMain = "LayoutMainEditText"
    private const val LayoutLoading = "LayoutLoading"
    private const val LayoutError = "LayoutError"
    private const val TextError = "TextError"
    private const val ButtonRetry = "ButtonRetry"
    private const val ProgressBarTag = "ProgressBarTag"
    private const val Checkmark = "Checkmark"
    private const val TextVerifyingLink = "TextVerifyingLink"
  }
}