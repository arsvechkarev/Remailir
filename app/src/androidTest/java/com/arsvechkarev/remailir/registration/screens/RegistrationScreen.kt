package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.arsvechkarev.remailir.R

class RegistrationScreen : Screen<RegistrationScreen>() {
  
  val editTextUsername = KEditText { withId(R.id.editTextUsername) }
  val buttonSignUp = KButton { withId(R.id.buttonSignUp) }
  val textError = KTextView { withId(R.id.textError) }
  
}