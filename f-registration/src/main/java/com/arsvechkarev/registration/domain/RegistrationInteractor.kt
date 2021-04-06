package com.arsvechkarev.registration.domain

import android.os.SystemClock
import core.Authenticator
import com.arsvechkarev.registration.presentation.RegistrationPresenter
import core.KeyValueStorage
import core.ThisUserInfoStorage
import core.model.User
import firebase.database.FirebaseDatabase
import firebase.database.UsersDatabaseSchema
import timerx.Timer
import timerx.TimerBuilder
import java.util.concurrent.TimeUnit

class RegistrationInteractor(
  private val schema: UsersDatabaseSchema,
  private val authenticator: Authenticator,
  private val database: FirebaseDatabase,
  private val thisUserInfoStorage: ThisUserInfoStorage,
  private val timerSaver: KeyValueStorage,
) {
  
  private var timer: Timer? = null
  
  fun isSignInWithEmailLink(emailLink: String): Boolean {
    return authenticator.isSignInWithEmailLink(emailLink)
  }
  
  suspend fun isNoEmailSaved(): Boolean {
    return thisUserInfoStorage.getEmailOrNull() == null
  }
  
  suspend fun getSavedEmail(): String {
    return thisUserInfoStorage.getEmailOrNull() ?: error("Email is not saved")
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
        //        onTimerStarted(thisUserInfoStorage.getEmailOrNull()!!)
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
    authenticator.sendSignInLinkToEmail(email)
    thisUserInfoStorage.saveEmail(email)
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
    val user = User(username)
    val usernames = database.getList(schema.allUsernamesPath)
    usernames.add(username)
    database.setValues(
      mapOf(
        schema.allUsernamesPath to usernames,
        schema.emailPath(user) to "",
        schema.friendsPath(user) to "",
        schema.friendsRequestsFromUserPath(user) to "",
        schema.friendsRequestsToUserPath(user) to ""
      )
    )
    thisUserInfoStorage.saveUsername(username)
  }
  
  fun shouldSwitchToMainScreen(): Boolean {
    return authenticator.isUserLoggedIn()
  }
  
  fun release() {
    timer?.release()
    timer = null
  }
}