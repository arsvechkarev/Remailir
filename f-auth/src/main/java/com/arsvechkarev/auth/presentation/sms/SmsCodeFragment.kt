package com.arsvechkarev.auth.presentation.sms

import android.os.Bundle
import android.view.View
import com.arsvechakrev.auth.R
import core.base.BaseFragment
import core.base.entranceActivity
import core.util.popBackStack
import core.util.showKeyboard
import kotlinx.android.synthetic.main.fragment_sms_code.sixDigitsCodeLayout
import kotlinx.android.synthetic.main.fragment_sms_code.theToolbar

class SmsCodeFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_sms_code
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    theToolbar.onBackClick {
      popBackStack()
    }
    sixDigitsCodeLayout.onDone {
      entranceActivity.onCheckCode(it)
    }
    sixDigitsCodeLayout.setOnClickListener {
      showKeyboard()
      sixDigitsCodeLayout.start()
    }
  }
  
  override fun onResume() {
    super.onResume()
    sixDigitsCodeLayout.start()
  }
}
