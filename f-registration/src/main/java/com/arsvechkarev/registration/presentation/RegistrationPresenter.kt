package com.arsvechkarev.registration.presentation

import com.arsvechkarev.registration.R
import com.arsvechkarev.registration.domain.RegistrationInteractor
import com.arsvechkarev.registration.domain.Validator
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_EMPTY
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_INVALID
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_CONTAINS_PROHIBITED_SYMBOLS
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_EMPTY
import com.google.firebase.auth.FirebaseAuthActionCodeException
import core.Dispatchers
import core.ui.BasePresenter
import core.ui.MIN_NETWORK_DELAY
import kotlinx.coroutines.delay

class RegistrationPresenter(
  private val interactor: RegistrationInteractor,
  dispatchers: Dispatchers
) : BasePresenter<RegistrationView>(dispatchers) {
  
  fun figureOutScreenToGo(emailLink: String? = null) {
    if (emailLink != null && interactor.isSignInWithEmailLink(emailLink)) {
      handleSignInWithEmailLink(emailLink)
    } else {
      interactor.figureOutInitialState(
        onTimerTick = { viewState.showTimeTicking(it) },
        onTimerFinish = { viewState.showTimeHasRunOut() },
        onTimerStarted = { viewState.showEmailSent(it) },
        onTimerDoesNotStart = { viewState.showInitialState() }
      )
    }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  fun sendEmailLink(email: String) {
    if (Validator.isEmailIncorrect(email)) {
      when (Validator.validateEmail(email)) {
        EMAIL_EMPTY -> viewState.showTextIsIncorrect(R.string.error_email_is_empty)
        EMAIL_INVALID -> viewState.showTextIsIncorrect(R.string.error_email_is_invalid)
      }
      return
    }
    coroutine {
      try {
        viewState.showLoading()
        delay(MIN_NETWORK_DELAY)
        interactor.sendEmailLink(email,
          onTimerTick = { viewState.showTimeTicking(it) },
          onTimerFinish = { viewState.showTimeHasRunOut() }
        )
        viewState.showEmailSent(email)
      } catch (e: Throwable) {
        viewState.showFailure(e)
      }
    }
  }
  
  fun continueRegistration() {
    if (interactor.shouldSwitchToMainScreen()) {
      viewState.switchToMainScreen()
    } else {
      viewState.showEnterUserNameLayout()
    }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  fun onEnteredUsername(username: String) {
    if (Validator.isUsernameIncorrect(username)) {
      when (Validator.validateUsername(username)) {
        USERNAME_EMPTY -> viewState.showTextIsIncorrect(R.string.error_username_is_empty)
        USERNAME_CONTAINS_PROHIBITED_SYMBOLS -> viewState.showTextIsIncorrect(
          R.string.error_username_contains_prohibited_symbols)
      }
      return
    }
    coroutine {
      try {
        viewState.showLoading()
        delay(MIN_NETWORK_DELAY)
        interactor.saveUsername(username)
        viewState.showSignedIn()
      } catch (e: Throwable) {
        if (e is core.UsernameAlreadyExistsException) {
          viewState.showTextIsIncorrect(R.string.error_username_already_exists)
        } else {
          viewState.showFailure(e)
        }
      }
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    interactor.release()
  }
  
  private fun handleSignInWithEmailLink(emailLink: String) {
    if (interactor.isNoEmailSaved()) {
      viewState.showNoEmailSaved()
      return
    }
    val email = interactor.getSavedEmail()
    coroutine {
      try {
        viewState.showVerifyingLink()
        interactor.signInWithEmailLink(email, emailLink)
        viewState.showSuccessfullyVerified()
      } catch (e: Throwable) {
        if (e is FirebaseAuthActionCodeException) {
          viewState.showVerificationLinkExpired(email)
        } else {
          viewState.showEmailVerificationFailure(e)
        }
      }
    }
  }
  
  companion object {
    
    const val TIMER_TIME_MILLIS = 60 * 1000L
    const val TIMER_FORMAT = "MM:SS"
    const val TIMER_KEY = "TimeLeftForWaiting"
    const val TIMER_FILENAME = "ResendEmailTimerSaver"
  }
}