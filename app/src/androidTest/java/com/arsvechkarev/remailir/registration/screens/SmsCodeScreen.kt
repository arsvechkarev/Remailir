package com.arsvechkarev.remailir.registration.screens

import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.image.KImageView
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView
import com.arsvechkarev.remailir.R

class SmsCodeScreen : Screen<SmsCodeScreen>() {
  
  val imageBack = KImageView { withId(R.id.imageBack) }
  
  val editTextSmsCode = KEditText { withId(R.id.editTextHiddenCode) }
  val textError = KTextView { withId(R.id.textError) }
  
}