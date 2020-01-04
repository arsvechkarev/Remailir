package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.arsvechkarev.remailir.R

class StartPhoneScreen : Screen<StartPhoneScreen>() {
  
  val editTextPhone = KEditText { withId(R.id.editTextPhone) }
  val buttonNext = KButton { withId(R.id.buttonNext) }
  val layoutCountryCode = KView { withId(R.id.layoutCountryCode) }
  val textCountryCode = KView { withId(R.id.textCountryCode) }
  val textError = KView { withId(R.id.textError) }
  
}