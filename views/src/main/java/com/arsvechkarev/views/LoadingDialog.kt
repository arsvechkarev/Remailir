package com.arsvechkarev.views

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment

class LoadingDialog : DialogFragment() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isCancelable = false
  }
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_corners_dialog)
    return inflater.inflate(R.layout.loading_dialog, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val imageHourglass = view.findViewById<ImageView>(R.id.imageHourglass)
    val drawable = imageHourglass.drawable as AnimatedVectorDrawable
    drawable.start()
  }
}