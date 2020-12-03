package com.arsvechkarev.registration.di

import com.arsvechkarev.core.SharedPrefsStorage
import com.arsvechkarev.core.concurrency.AndroidDispatchers
import com.arsvechkarev.firebase.auth.FirebaseAuthenticator
import com.arsvechkarev.firebase.auth.SharedPrefsEmailSaver
import com.arsvechkarev.firebase.database.FirebaseDatabaseImpl
import com.arsvechkarev.firebase.database.PathDatabaseSchema
import com.arsvechkarev.registration.domain.RegistrationInteractor
import com.arsvechkarev.registration.presentation.RegistrationPresenter
import com.arsvechkarev.registration.presentation.RegistrationPresenter.Companion.TIMER_FILENAME
import com.arsvechkarev.viewdsl.ContextHolder

object RegistrationInjector {
  
  fun providePresenter(): RegistrationPresenter {
    val context = ContextHolder.context
    val profileSaver = SharedPrefsEmailSaver(context)
    val timerSaver = SharedPrefsStorage(TIMER_FILENAME, context)
    val interactor = RegistrationInteractor(
      PathDatabaseSchema,
      FirebaseAuthenticator,
      FirebaseDatabaseImpl(AndroidDispatchers),
      profileSaver,
      timerSaver
    )
    return RegistrationPresenter(interactor, AndroidDispatchers)
  }
}