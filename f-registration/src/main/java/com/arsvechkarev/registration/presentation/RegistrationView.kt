package com.arsvechkarev.registration.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface RegistrationView : MvpView {
  
  fun showInitialState()
  
  fun showEnterUserNameLayout()
  
  fun animateToInitialState()
  
  fun showLoading()
  
  fun showVerifyingLink()
  
  fun showSuccessfullyVerified()
  
  fun showSignedIn()
  
  fun showTextIsIncorrect(messageResId: Int)
  
  fun showNoEmailSaved()
  
  fun showVerificationLinkExpired(email: String)
  
  fun showEmailVerificationFailure(e: Throwable)
  
  fun showFailure(e: Throwable)
  
  fun showTimeTicking(time: CharSequence)
  
  fun showTimeHasRunOut()
  
  fun showEmailSent(email: String?)
}