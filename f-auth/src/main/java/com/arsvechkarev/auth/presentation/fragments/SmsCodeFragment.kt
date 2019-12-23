package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.view.View
import com.arsvechakrev.auth.R
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.string
import kotlinx.android.synthetic.main.fragment_sms_code.buttonCheckCode
import kotlinx.android.synthetic.main.fragment_sms_code.editTextCode

class SmsCodeFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_sms_code
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    buttonCheckCode.setOnClickListener {
      entranceActivity.onCheckCode(editTextCode.string())
    }
  }
}
