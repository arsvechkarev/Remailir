package com.arsvechkarev.registration.di

import com.arsvechkarev.core.SharedPrefsStorage
import com.arsvechkarev.core.auth.FirebaseAuthenticator
import com.arsvechkarev.core.auth.SharedPrefsAuthEmailSaver
import com.arsvechkarev.core.concurrency.AndroidSchedulers
import com.arsvechkarev.registration.presentation.RegistrationPresenter
import com.arsvechkarev.registration.presentation.RegistrationPresenter.Companion.TIMER_FILENAME
import com.arsvechkarev.registration.presentation.RegistrationScreen

object RegistrationInjector {
  
  fun providePresenter(screen: RegistrationScreen): RegistrationPresenter {
    val context = screen.contextNonNull
    val emailSaver = SharedPrefsAuthEmailSaver(context)
    val timerSaver = SharedPrefsStorage(TIMER_FILENAME, context)
    return RegistrationPresenter(
      FirebaseAuthenticator,
      emailSaver,
      timerSaver,
      screen.navigator,
      AndroidSchedulers
    )
  }
}