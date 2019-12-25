package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import com.arsvechakrev.auth.R
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.showKeyboard
import kotlinx.android.synthetic.main.fragment_sms_code.code1
import kotlinx.android.synthetic.main.fragment_sms_code.code2
import kotlinx.android.synthetic.main.fragment_sms_code.code3
import kotlinx.android.synthetic.main.fragment_sms_code.code4
import kotlinx.android.synthetic.main.fragment_sms_code.code5
import kotlinx.android.synthetic.main.fragment_sms_code.code6
import kotlinx.android.synthetic.main.fragment_sms_code.editTextHiddenCode
import kotlinx.android.synthetic.main.fragment_sms_code.layoutDigits

class SmsCodeFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_sms_code
  
  private val smsCodeDelegate by lazy {
    SmsCodeDelegate(code1, code2, code3, code4, code5, code6, editTextHiddenCode) {
      entranceActivity.onCheckCode(it)
    }
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    layoutDigits.setOnClickListener {
      smsCodeDelegate.requestFocus()
    }
    smsCodeDelegate.start()
    showKeyboard()
  }
}
