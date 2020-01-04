package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView
import com.arsvechkarev.remailir.R

class PhoneScreen : Screen<PhoneScreen>() {
  
  val editTextPhone = KEditText { withId(R.id.editTextPhone) }
  val buttonNext = KButton { withId(R.id.buttonNext) }
  val layoutCountryCode = KView { withId(R.id.layoutCountryCode) }
  val textCountryCode = KTextView { withId(R.id.textCountryCode) }
  
}