package com.arsvechkarev.registration.presentation

import android.view.View
import com.arsvechkarev.core.MAX_SYMBOLS_FOR_NICKNAME
import com.arsvechkarev.core.extenstions.getMessageRes
import com.arsvechkarev.core.extenstions.getRegistrationMessageRes
import com.arsvechkarev.core.extenstions.ifNotNull
import com.arsvechkarev.core.extenstions.moxyPresenter
import com.arsvechkarev.core.navigation.Screen
import com.arsvechkarev.registration.R
import com.arsvechkarev.registration.di.RegistrationInjector
import com.arsvechkarev.registration.layout.SignInButton
import com.arsvechkarev.registration.layout.SignInLayout
import com.arsvechkarev.registration.layout.SignInLayout.Companion.ButtonOpenEmailApp
import com.arsvechkarev.registration.layout.SignInLayout.Companion.ButtonRetry
import com.arsvechkarev.registration.layout.SignInLayout.Companion.ButtonSignIn
import com.arsvechkarev.registration.layout.SignInLayout.Companion.Checkmark
import com.arsvechkarev.registration.layout.SignInLayout.Companion.EditTextTag
import com.arsvechkarev.registration.layout.SignInLayout.Companion.ImageLogo
import com.arsvechkarev.registration.layout.SignInLayout.Companion.LayoutError
import com.arsvechkarev.registration.layout.SignInLayout.Companion.LayoutLoading
import com.arsvechkarev.registration.layout.SignInLayout.Companion.ProgressBarTag
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextDescription
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextEditTextError
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextError
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextLinkWasSent
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextLoading
import com.arsvechkarev.registration.layout.SignInLayout.Companion.TextTimer
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.setMaxLength
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.views.CheckmarkView
import timber.log.Timber

class RegistrationScreen : Screen(), RegistrationView {
  
  override fun buildLayout() = withViewBuilder {
    SignInLayout(context)
  }
  
  private val presenter by moxyPresenter { RegistrationInjector.providePresenter(this) }
  
  override fun onInit() {
    presenter.figureOutScreenToGo(activityNonNull.intent)
    view(ButtonOpenEmailApp).onClick { presenter.openEmailApp() }
  }
  
  override fun showInitialState() {
    editText(EditTextTag).setHint(R.string.hint_edit_text_email)
    editText(EditTextTag).isEnabled = true
    editText(EditTextTag).requestFocus()
    viewAs<SignInButton>(ButtonSignIn).title.text(R.string.text_continue)
    view(ButtonSignIn).onClick {
      val email = editText(EditTextTag).text.toString().trim()
      presenter.sendEmailLink(email)
    }
  }
  
  override fun showEnterUserNameLayout() {
    view(LayoutLoading).animateInvisible()
    view(LayoutError).animateInvisible()
    withMainLayout { animateVisible() }
    textView(TextDescription).text(R.string.text_create_username)
    editText(EditTextTag).setMaxLength(MAX_SYMBOLS_FOR_NICKNAME)
    editText(EditTextTag).setHint(R.string.hint_edit_text_username)
    editText(EditTextTag).requestFocus()
    editText(EditTextTag).isEnabled = true
    viewAs<SignInButton>(ButtonSignIn).title.text(R.string.text_sign_in)
    viewAs<SignInButton>(ButtonSignIn).isClickable = true
    viewAs<SignInButton>(ButtonSignIn).onClick {
      val username = editText(EditTextTag).text.toString().trim()
      presenter.onEnteredUsername(username)
    }
  }
  
  override fun animateToInitialState() {
    view(LayoutError).animateInvisible()
    view(ButtonSignIn).animateVisible()
    withMainLayout { animateVisible() }
    view(EditTextTag).isEnabled = true
  }
  
  override fun showLoading() {
    withMainLayout { animateVisible() }
    view(LayoutError).animateInvisible()
    view(TextEditTextError).animateInvisible()
    view(ButtonSignIn).isClickable = false
    viewAs<SignInButton>(ButtonSignIn).showProgress()
  }
  
  override fun showVerifyingLink() {
    withMainLayout { invisible() }
    view(LayoutError).invisible()
    view(LayoutLoading).animateVisible()
  }
  
  override fun showSuccessfullyVerified() {
    view(ProgressBarTag).animateInvisible(andThen = {
      textView(TextLoading).text(R.string.text_successfully_verified)
      viewAs<CheckmarkView>(Checkmark).animateCheckmark(andThen = {
        view(LayoutLoading).animateInvisible(andThen = {
          presenter.continueRegistration()
        })
      })
    })
  }
  
  override fun showSignedIn() {
    hideKeyboard()
    editText(EditTextTag).isEnabled = false
    withMainLayout { animateInvisible() }
    view(LayoutLoading).animateVisible()
    textView(TextLoading).text(R.string.text_successfully_signed_in)
    viewAs<CheckmarkView>(Checkmark).animateCheckmark(andThen = {
      view(LayoutLoading).animateInvisible(andThen = {
        presenter.continueRegistration()
      })
    })
  }
  
  override fun showTextIsIncorrect(messageResId: Int) {
    textView(TextEditTextError).text(messageResId)
    textView(TextEditTextError).animateVisible()
    viewAs<SignInButton>(ButtonSignIn).hideProgress()
    viewAs<SignInButton>(ButtonSignIn).isClickable = true
  }
  
  override fun showNoEmailSaved() {
    showFailureLayout(
      R.string.error_while_checking_link,
      R.string.text_retry,
      onClickAction = { animateToInitialState() }
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
    Timber.d(e, "Error: verification")
    showFailureLayout(
      e.getMessageRes(),
      R.string.text_retry,
      onClickAction = {
        presenter.figureOutScreenToGo(activityNonNull.intent)
      })
  }
  
  override fun showFailure(e: Throwable) {
    Timber.d(e, "Error")
    view(ButtonSignIn).isClickable = true
    textView(TextEditTextError).text(e.getRegistrationMessageRes())
    textView(TextEditTextError).animateVisible()
    viewAs<SignInButton>(ButtonSignIn).hideProgress()
  }
  
  override fun showTimeTicking(time: CharSequence) {
    textView(TextTimer).text(getString(R.string.number_resend_link, time))
  }
  
  override fun showTimeHasRunOut() {
    withTimerLayout { animateInvisible() }
    view(ButtonSignIn).isEnabled = true
    view(ButtonSignIn).isClickable = true
    view(EditTextTag).isEnabled = true
  }
  
  override fun showEmailSent(email: String?) {
    email.ifNotNull { editText(EditTextTag).text(it) }
    viewAs<SignInButton>(ButtonSignIn).title.text(R.string.text_sign_in)
    viewAs<SignInButton>(ButtonSignIn).hideProgress()
    withTimerLayout { animateVisible() }
    view(ButtonSignIn).isEnabled = false
    view(EditTextTag).isEnabled = false
  }
  
  private fun showFailureLayout(
    textErrorRes: Int,
    textButtonRetryRes: Int,
    onClickAction: () -> Unit
  ) {
    withMainLayout { animateInvisible() }
    withTimerLayout { animateInvisible() }
    view(ButtonSignIn).animateInvisible()
    view(LayoutLoading).animateInvisible()
    view(LayoutError).animateVisible()
    textView(TextError).text(textErrorRes)
    textView(ButtonRetry).text(textButtonRetryRes)
    textView(ButtonRetry).onClick(onClickAction)
  }
  
  private fun withMainLayout(block: View.() -> Unit) {
    view(ImageLogo).apply(block)
    view(TextDescription).apply(block)
    view(EditTextTag).apply(block)
    view(TextEditTextError).apply(block)
    view(ButtonSignIn).apply(block)
  }
  
  private fun withTimerLayout(block: View.() -> Unit) {
    view(TextLinkWasSent).apply(block)
    view(TextTimer).apply(block)
    view(ButtonOpenEmailApp).apply(block)
  }
}