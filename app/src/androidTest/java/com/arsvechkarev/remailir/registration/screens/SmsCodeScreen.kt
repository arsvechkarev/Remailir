package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.common.views.KView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.screen.Screen
import com.arsvechkarev.remailir.R

class SmsCodeScreen : Screen<SmsCodeScreen>() {
  
  val imageBack = KImageView { withId(R.id.imageBack) }
  
  val editTextSmsCode = KEditText { withId(R.id.editTextHiddenCode) }
  val textError = KView { withId(R.id.textError) }
  
}