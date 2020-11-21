package com.arsvechkarev.registration.domain

import android.os.SystemClock
import com.arsvechkarev.core.KeyValueStorage
import com.arsvechkarev.firebase.auth.Authenticator
import com.arsvechkarev.firebase.auth.EmailSaver
import com.arsvechkarev.firebase.database.Database
import com.arsvechkarev.registration.presentation.RegistrationPresenter
import timerx.Timer
import timerx.TimerBuilder
import java.util.concurrent.TimeUnit

class RegistrationInteractor(
  private val authenticator: Authenticator,
  private val database: Database,
  private val emailSaver: EmailSaver,
  private val timerSaver: KeyValueStorage,
) {
  
  private var timer: Timer? = null
  
  fun isSignInWithEmailLink(emailLink: String): Boolean {
    return authenticator.isSignInWithEmailLink(emailLink)
  }
  
  fun isNoEmailSaved(): Boolean {
    return emailSaver.getEmail() == null
  }
  
  fun getSavedEmail(): String {
    return emailSaver.getEmail()!!
  }
  
  fun figureOutInitialState(
    onTimerTick: (time: CharSequence) -> Unit,
    onTimerFinish: () -> Unit,
    onTimerStarted: (email: String?) -> Unit,
    onTimerDoesNotStart: () -> Unit
  ) {
    if (timerSaver.hasLong(RegistrationPresenter.TIMER_KEY)) {
      val builder = TimerBuilder()
          .startFormat(RegistrationPresenter.TIMER_FORMAT)
          .onTick { time -> onTimerTick(time) }
          .onFinish {
            onTimerFinish()
            timerSaver.execute { clear() }
          }
      val timeLeft = timerSaver.getLong(
        RegistrationPresenter.TIMER_KEY) - SystemClock.elapsedRealtime()
      if (timeLeft > 0) {
        builder.startTime(timeLeft, TimeUnit.MILLISECONDS)
        timer = builder.build()
        timer!!.start()
        onTimerStarted(emailSaver.getEmail())
      }
    } else {
      onTimerDoesNotStart()
    }
  }
  
  suspend fun sendEmailLink(
    email: String,
    onTimerTick: (CharSequence) -> Unit,
    onTimerFinish: () -> Unit
  ) {
    authenticator.sendSignInLinkToEmail(email, com.arsvechkarev.firebase.auth.AuthSettings)
    emailSaver.saveEmail(email)
    val timeInFuture = SystemClock.elapsedRealtime() + TimeUnit.MINUTES.toMillis(1)
    timerSaver.execute { putLong(RegistrationPresenter.TIMER_KEY, timeInFuture) }
    timer = TimerBuilder()
        .startFormat(RegistrationPresenter.TIMER_FORMAT)
        .startTime(RegistrationPresenter.TIMER_TIME_MILLIS, TimeUnit.MILLISECONDS)
        .onTick { time -> onTimerTick(time) }
        .onFinish {
          onTimerFinish()
          timerSaver.execute { clear() }
        }
        .build()
    timer!!.start()
  }
  
  suspend fun signInWithEmailLink(email: String, emailLink: String) {
    timerSaver.execute { clear() }
    authenticator.signInWithEmailLink(email, emailLink)
  }
  
  suspend fun saveUsername(username: String) {
    database.saveUser(username, emailSaver.getEmail()!!)
    authenticator.saveUsername(username)
  }
  
  fun shouldSwitchToMainScreen(): Boolean {
    return authenticator.userHasDisplayName()
  }
  
  fun release() {
    timer?.release()
    timer = null
  }
}