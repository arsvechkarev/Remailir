package com.arsvechkarev.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class LoadingDialog : DialogFragment() {
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.loading_dialog, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    isCancelable = false
    val title = arguments?.get(KEY_TITLE) as String
    val textTitle = view.findViewById<TextView>(R.id.textLoadingTitle)
    textTitle.text = title
  }
  
  companion object {
    const val KEY_TITLE = "KEY_TITLE"
    
    fun create(title: String): LoadingDialog {
      return LoadingDialog().apply {
        arguments = Bundle().apply {
          putString(KEY_TITLE, title)
        }
      }
    }
  }
}