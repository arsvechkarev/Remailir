package com.arsvechkarev.views.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import animation.animateVectorDrawable
import com.arsvechkarev.views.R
import core.extensions.findImage

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
    dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_rounded_corners_dialog)
    dialog?.window?.attributes?.windowAnimations =
      R.style.LoadingDialogAnimation
    return inflater.inflate(R.layout.dialog_loading, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val imageHourglass = view.findImage(R.id.imageHourglass)
    imageHourglass.animateVectorDrawable()
  }
}