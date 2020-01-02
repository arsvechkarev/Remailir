package com.arsvechkarev.auth.presentation.phone

import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import com.google.i18n.phonenumbers.PhoneNumberUtil
import log.Loggable
import log.log

open class PhoneFormatter(countryCode: String) : TextWatcher, Loggable {
  
  override val logTag = "PhoneFormatter"
  
  private var selfChange = false
  private val formatter = PhoneNumberUtil.getInstance().getAsYouTypeFormatter(countryCode)
  
  override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    log { "=========================== " }
    log { "========== before ========= " }
    log { "=========================== " }
    log { "s = $s" }
    log { "start = $start" }
    log { "after = $after" }
    log { "count = $count" }
  }
  
  override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    log { "========== on ========= " }
    log { "s = $s" }
    log { "start = $start" }
    log { "before = $before" }
    log { "count = $count" }
  }
  
  override fun afterTextChanged(s: Editable) {
    if (selfChange) {
      return
    }
    formatter.rememberedPosition
    log { "========== after ========= " }
    log { "s = $s" }
    
  }
  
  private fun reformat(s: CharSequence, cursor: Int): String {
    val curIndex = cursor - 1
    var formatted = ""
    formatter.clear()
    var lastNonSeparator = '0'
    var hasCursor = false
    val len = s.length
    for (i in 0 until len) {
      val c = s[i]
      if (PhoneNumberUtils.isNonSeparator(c)) {
        if (lastNonSeparator.toInt() != 0) {
          formatted = getFormattedNumber(lastNonSeparator, hasCursor)
          hasCursor = false
        }
        lastNonSeparator = c
      }
      if (i == curIndex) {
        hasCursor = true
      }
    }
    if (lastNonSeparator.toInt() != 0) {
      formatted = getFormattedNumber(lastNonSeparator, hasCursor)
    }
    return formatted
  }
  
  private fun getFormattedNumber(lastNonSeparator: Char, hasCursor: Boolean): String {
    return if (hasCursor)
      formatter.inputDigitAndRememberPosition(lastNonSeparator)
    else
      formatter.inputDigit(lastNonSeparator)
  }
}