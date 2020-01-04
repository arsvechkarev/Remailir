package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.arsvechkarev.remailir.R

class RegistrationScreen : Screen<RegistrationScreen>() {
  
  val editTextUsername = KEditText { withId(R.id.editTextUsername) }
  val buttonSignUp = KButton { withId(R.id.buttonSignUp) }
  val textError = KView { withId(R.id.textError) }
  
}