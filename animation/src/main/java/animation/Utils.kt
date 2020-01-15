package animation

import android.animation.Animator
import android.animation.AnimatorInflater
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment

fun Fragment.loadAnimation(@AnimatorRes resId: Int): Animator {
  return AnimatorInflater.loadAnimator(requireContext(), resId)
}
