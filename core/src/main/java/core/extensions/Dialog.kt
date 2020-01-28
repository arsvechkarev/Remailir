package core.extensions

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun DialogFragment?.dismissSafely() {
  if (this != null && isAdded) {
    dismiss()
  }
}

fun Fragment.show(fragment: DialogFragment) {
  fragment.show(childFragmentManager, null)
}