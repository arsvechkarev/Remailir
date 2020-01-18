package com.arsvechkarev.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.arsvechkarev.views.R

class ConfirmationDialog : DialogFragment() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isCancelable = false
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_corners_dialog)
    return inflater.inflate(R.layout.dialog_confirmation, container, false)
  }
  
  
}