package com.arsvechkarev.auth.presentation.phone

import android.telephony.PhoneNumberUtils
import com.google.i18n.phonenumbers.PhoneNumberUtil
import org.junit.Test


class PhoneFragmentTest {
  
  @Test
  fun name() {
    println(PhoneNumberUtils.formatNumber("123654", "US"))
  }
  
  @Test
  fun phoneFormattingTest() {
    
    val phoneNumberUtil = PhoneNumberUtil.getInstance()
    
    val formatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter("RU")
    formatter.inputDigit('9')
    formatter.inputDigit('7')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    formatter.inputDigit('1')
    println(formatter.inputDigit('8'))
    
  }
}