package com.arsvechkarev.registration.presentation

import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.firebase.UsernameAlreadyExistsException
import com.arsvechkarev.registration.R
import com.arsvechkarev.registration.domain.RegistrationInteractor
import com.arsvechkarev.registration.domain.Validator
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_EMPTY
import com.arsvechkarev.registration.domain.Validator.EmailState.EMAIL_INVALID
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_CONTAINS_PROHIBITED_SYMBOLS
import com.arsvechkarev.registration.domain.Validator.UsernameState.USERNAME_EMPTY
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.coroutines.delay
import moxy.InjectViewState

@InjectViewState
class RegistrationPresenter(
  private val interactor: RegistrationInteractor,
  dispatchers: Dispatchers
) : BasePresenter<RegistrationView>(dispatchers) {
  
  fun figureOutScreenToGo(emailLink: String? = null) {
    if (emailLink != null && interactor.isSignInWithEmailLink(emailLink)) {
      handleSignInWithEmailLink(emailLink)
    } else {
      interactor.figureOutInitialState(
        onTimerTick = { updateView { showTimeTicking(it) } },
        onTimerFinish = { updateView { showTimeHasRunOut() } },
        onTimerStarted = { updateView { showEmailSent(it) } },
        onTimerDoesNotStart = { updateView { showInitialState() } }
      )
    }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  fun sendEmailLink(email: String) {
    if (Validator.isEmailIncorrect(email)) {
      when (Validator.validateEmail(email)) {
        EMAIL_EMPTY -> updateView { showTextIsIncorrect(R.string.error_email_is_empty) }
        EMAIL_INVALID -> updateView { showTextIsIncorrect(R.string.error_email_is_invalid) }
      }
      return
    }
    coroutine {
      try {
        updateView { showLoading() }
        delay(MIN_NETWORK_DELAY)
        interactor.sendEmailLink(email,
          onTimerTick = { updateView { showTimeTicking(it) } },
          onTimerFinish = { updateView { showTimeHasRunOut() } }
        )
        updateView { showEmailSent(email) }
      } catch (e: Throwable) {
        updateView { showFailure(e) }
      }
    }
  }
  
  fun continueRegistration() {
    if (interactor.shouldSwitchToMainScreen()) {
      updateView { switchToMainScreen() }
    } else {
      updateView { showEnterUserNameLayout() }
    }
  }
  
  @Suppress("NON_EXHAUSTIVE_WHEN")
  fun onEnteredUsername(username: String) {
    if (Validator.isUsernameIncorrect(username)) {
      when (Validator.validateUsername(username)) {
        USERNAME_EMPTY -> updateView { showTextIsIncorrect(R.string.error_username_is_empty) }
        USERNAME_CONTAINS_PROHIBITED_SYMBOLS -> updateView {
          showTextIsIncorrect(R.string.error_username_contains_prohibited_symbols)
        }
      }
      return
    }
    coroutine {
      try {
        updateView { showLoading() }
        delay(MIN_NETWORK_DELAY)
        interactor.saveUsername(username)
        updateView { showSignedIn() }
      } catch (e: Throwable) {
        if (e is UsernameAlreadyExistsException) {
          updateView { showTextIsIncorrect(R.string.error_username_already_exists) }
        } else {
          updateView { showFailure(e) }
        }
      }
    }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    println("lalala")
    interactor.release()
  }
  
  private fun handleSignInWithEmailLink(emailLink: String) {
    if (interactor.isNoEmailSaved()) {
      updateView { showNoEmailSaved() }
      return
    }
    val email = interactor.getSavedEmail()
    coroutine {
      try {
        updateView { showVerifyingLink() }
        interactor.signInWithEmailLink(email, emailLink)
        updateView { showSuccessfullyVerified() }
      } catch (e: Throwable) {
        if (e is FirebaseAuthActionCodeException) {
          updateView { showVerificationLinkExpired(email) }
        } else {
          updateView { showEmailVerificationFailure(e) }
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