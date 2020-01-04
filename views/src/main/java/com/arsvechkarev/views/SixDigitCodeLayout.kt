package com.arsvechkarev.views

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.isDigitsOnly

class SixDigitCodeLayout @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
  
  private var blockOnDone: (String) -> Unit = {}
  private val hiddenEditText by lazy { findViewById<EditText>(R.id.editTextHiddenCode) }
  
  private val textCodes by lazy {
    arrayOf<TextView>(
      findViewById(R.id.textCode1),
      findViewById(R.id.textCode2),
      findViewById(R.id.textCode3),
      findViewById(R.id.textCode4),
      findViewById(R.id.textCode5),
      findViewById(R.id.textCode6)
    )
  }
  
  private val underscores by lazy {
    arrayOf<View>(
      findViewById(R.id.underscore1),
      findViewById(R.id.underscore2),
      findViewById(R.id.underscore3),
      findViewById(R.id.underscore4),
      findViewById(R.id.underscore5),
      findViewById(R.id.underscore6)
    )
  }
  
  init {
    inflate(context, R.layout.layout_six_digit_code, this)
    val typedArray =
      context.theme.obtainStyledAttributes(attrs, R.styleable.SixDigitCodeLayout, defStyleAttr, 0)
    try {
      textCodes.setTextColorIfNeeded(typedArray, R.styleable.SixDigitCodeLayout_digitsTextColor)
      underscores.setBackgroundIfNeeded(typedArray, R.styleable.SixDigitCodeLayout_underscoresColor)
    } finally {
      typedArray.recycle()
    }
  }
  
  private var code = StringBuilder()
  private var currentPosition = 0
  
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
            blockOnDone(code.toString())
          }
          addDigit()
        }
      }
    }
    
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
      lastLength = s.length
    }
    
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    
  }
  
  fun start() {
    requestFocus()
    hiddenEditText.addTextChangedListener(customTextWatcher)
  }
  
  fun requestEditTextFocus() {
    hiddenEditText.requestFocus()
  }
  
  fun enable() {
    hiddenEditText.isEnabled = true
  }
  
  fun disable() {
    hiddenEditText.isEnabled = false
  }
  
  fun onDone(blockOnDone: (String) -> Unit) {
    this.blockOnDone = blockOnDone
  }
  
  private fun removeDigit() {
    textCodes[currentPosition].text = ""
  }
  
  private fun addDigit() {
    textCodes[currentPosition - 1].text = code.last().toString()
  }
  
}