package com.arsvechkarev.auth.presentation.sms

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import log.Loggable
import log.log


class SmsCodeDelegate(
  textCode1: TextView,
  textCode2: TextView,
  textCode3: TextView,
  textCode4: TextView,
  textCode5: TextView,
  textCode6: TextView,
  private val hiddenEditText: EditText,
  private val onDone: (String) -> Unit
) : Loggable {
  
  override val logTag: String
    get() = "SmsCodeDelegate"
  
  private var code = StringBuilder()
  private var currentPosition = 0
  
  private val textViews = listOf(
    textCode1, textCode2, textCode3, textCode4, textCode5, textCode6
  )
  
  fun start() {
    requestFocus()
    hiddenEditText.addTextChangedListener(customTextWatcher)
  }
  
  fun requestFocus() {
    hiddenEditText.requestFocus()
  }
  
  fun enable() {
    hiddenEditText.isEnabled = true
  }
  
  fun disable() {
    hiddenEditText.isEnabled = false
  }
  
  private val customTextWatcher = object : TextWatcher {
    
    private var lastLength: Int = 0
    
    override fun afterTextChanged(s: Editable) {
      if (lastLength > s.length) {
        currentPosition--
        code.delete(code.length - 1, code.length)
        removeDigit()
      } else {
        if (s.isDigitsOnly()) {
          currentPosition++
          code.append(s.last())
          if (code.length == 6) {
            onDone(code.toString())
          }
          addDigit()
        }
      }
      log { "code = $code" }
      log { "currentPosition = $currentPosition" }
    }
    
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
      lastLength = s.length
    }
  
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    
  }
  
  private fun removeDigit() {
    textViews[currentPosition].text = "_"
  }
  
  private fun addDigit() {
    textViews[currentPosition - 1].text = code.last().toString()
  }
}