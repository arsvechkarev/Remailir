package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import com.arsvechakrev.auth.R
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.showKeyboard
import core.extensions.string
import kotlinx.android.synthetic.main.fragment_sms_code.buttonCheckCode
import kotlinx.android.synthetic.main.fragment_sms_code.editTextCode
import kotlinx.android.synthetic.main.fragment_sms_code.editTextHiddenCode
import kotlinx.android.synthetic.main.fragment_sms_code.layoutDigits
import kotlinx.android.synthetic.main.fragment_sms_code.textCode1
import kotlinx.android.synthetic.main.fragment_sms_code.textCode2
import kotlinx.android.synthetic.main.fragment_sms_code.textCode3
import kotlinx.android.synthetic.main.fragment_sms_code.textCode4
import kotlinx.android.synthetic.main.fragment_sms_code.textCode5
import kotlinx.android.synthetic.main.fragment_sms_code.textCode6

class SmsCodeFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_sms_code
  
  private val smsCodeDelegate by lazy {
    SmsCodeDelegate(
      textCode1,
      textCode2,
      textCode3,
      textCode4,
      textCode5,
      textCode6,
      editTextHiddenCode
    ) {
      entranceActivity.onCheckCode(it)
    }
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    buttonCheckCode.setOnClickListener {
      entranceActivity.onCheckCode(editTextCode.string())
    }
    layoutDigits.setOnClickListener {
      smsCodeDelegate.requestFocus()
    }
    smsCodeDelegate.start()
    showKeyboard()
  }
}
