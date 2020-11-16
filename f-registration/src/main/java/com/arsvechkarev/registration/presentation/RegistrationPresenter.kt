package com.arsvechkarev.registration.presentation

import android.content.Intent
import android.os.SystemClock
import com.arsvechkarev.core.BasePresenter
import com.arsvechkarev.core.KeyValueStorage
import com.arsvechkarev.core.MIN_NETWORK_DELAY
import com.arsvechkarev.core.auth.AuthEmailSaver
import com.arsvechkarev.core.auth.AuthSettings
import com.arsvechkarev.core.auth.Authenticator
import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.navigation.Navigator
import com.arsvechkarev.registration.R
import com.google.firebase.auth.FirebaseAuthActionCodeException
import kotlinx.coroutines.delay
import moxy.InjectViewState
import timerx.Timer
import timerx.TimerBuilder
import java.util.concurrent.TimeUnit

@InjectViewState
class RegistrationPresenter(
  private val authenticator: Authenticator,
  private val emailSaver: AuthEmailSaver,
  private val timerSaver: KeyValueStorage,
  private var navigator: Navigator? = null,
  dispatchers: Dispatchers
) : BasePresenter<RegistrationView>(dispatchers) {
  
  private var timer: Timer? = null
  
  fun figureOutScreenToGo(intent: Intent) {
    val emailLink = intent.data.toString()
    when {
      authenticator.isSignInWithEmailLink(emailLink) -> handleSignInWithEmailLink(emailLink)
      else -> showInitialScreen()
    }
  }
  
  fun sendEmailLink(email: String) {
    if (isEmailIncorrect(email)) {
      return
    }
    coroutine {
      try {
        updateView { showLoading() }
        delay(MIN_NETWORK_DELAY)
        authenticator.sendSignInLinkToEmail(email, AuthSettings)
        emailSaver.saveEmail(email)
        updateView { showEmailSent(email) }
        initTimer()
      } catch (e: Throwable) {
        updateView { showFailure(e) }
      }
    }
  }
  
  fun goToMainFragment() {
    navigator?.switchToMainScreen()
  }
  
  fun animateToInitialState() {
    updateView { animateToInitialState() }
  }
  
  override fun onDestroy() {
    super.onDestroy()
    timer?.release()
    navigator = null
  }
  
  private fun handleSignInWithEmailLink(emailLink: String) {
    val email = emailSaver.getEmail()
    if (email == null) {
      updateView { showNoEmailSaved() }
      return
    }
    coroutine {
      try {
        updateView { showVerifyingLink() }
        authenticator.signInWithEmailLink(email, emailLink)
        updateView { showSuccessFullyVerified() }
      } catch (e: Throwable) {
        println("lala0 = $e")
        if (e is FirebaseAuthActionCodeException) {
          println("lala1")
          updateView { showVerificationLinkExpired(email) }
        } else {
          println("lala2")
          updateView { showEmailVerificationFailure(e) }
        }
      }
    }
  }
  
  private fun showInitialScreen() {
    if (timerSaver.hasLong(TIMER_KEY)) {
      val builder = TimerBuilder()
          .startFormat(TIMER_FORMAT)
          .onTick { time -> onTimerTick(time) }
          .onFinish { onTimerFinish() }
      val timeLeft = timerSaver.getLong(TIMER_KEY) - SystemClock.elapsedRealtime()
      val startTimer = timeLeft > 0
      if (startTimer) {
        builder.startTime(timeLeft, TimeUnit.MILLISECONDS)
      } else {
        builder.startTime(TIMER_TIME_MILLIS, TimeUnit.MILLISECONDS)
      }
      timer = builder.build()
      if (startTimer) {
        timer!!.start()
        updateView { showEmailSent(emailSaver.getEmail()) }
      } else {
        updateView { showInitialState() }
      }
    } else {
      updateView { showInitialState() }
    }
  }
  
  private fun initTimer() {
    val timeInFuture = SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(1)
    timerSaver.execute { putLong(TIMER_KEY, timeInFuture) }
    timer = TimerBuilder()
        .startFormat(TIMER_FORMAT)
        .startTime(TIMER_TIME_MILLIS, TimeUnit.MILLISECONDS)
        .onTick { time -> onTimerTick(time) }
        .onFinish { onTimerFinish() }
        .build()
    timer!!.start()
  }
  
  private fun onTimerTick(time: CharSequence) {
    updateView { showTimeTicking(time) }
  }
  
  private fun onTimerFinish() {
    updateView { showTimeHasRunOut() }
    timerSaver.execute { clear() }
  }
  
  private fun isEmailIncorrect(email: String): Boolean = when {
    email.isBlank() -> {
      updateView { showEmailIsIncorrect(R.string.error_email_is_empty) }
      true
    }
    !email.contains("@") -> {
      updateView { showEmailIsIncorrect(R.string.error_email_is_incorrect) }
      true
    }
    else -> false
  }
  
  companion object {
    
    const val TIMER_TIME_MILLIS = 60 * 1000L
    const val TIMER_FORMAT = "MM:SS"
    const val TIMER_KEY = "TimeLeftForWaiting"
    const val TIMER_FILENAME = "ResendEmailTimerSaver"
  }
}