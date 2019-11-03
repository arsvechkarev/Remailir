package com.arsvechkarev.signup.presentation

import android.widget.EditText
import com.arsvechkarev.core.extensions.afterTextChanged
import com.google.android.material.textfield.TextInputLayout

fun EditText.handleTyping(relatedInputLayout: TextInputLayout, after: () -> Unit = {}) {
  afterTextChanged {
    relatedInputLayout.error = null
    if (after != {}) after()
  }
}
