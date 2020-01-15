package animation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment

fun Fragment.loadAnimator(@AnimatorRes resId: Int): Animator {
  return AnimatorInflater.loadAnimator(requireContext(), resId)
}

fun View.loadAnimation(@AnimRes resId: Int): Animation {
  return AnimationUtils.loadAnimation(context, resId)
}

fun View.loadAnimationAnd(@AnimRes resId: Int, block: Animation.() -> Unit): Animation {
  val animation = AnimationUtils.loadAnimation(context, resId)
  animation.apply(block)
  return animation
}

fun Animation.doAfterAnimation(block: Animation.() -> Unit) {
  this.setAnimationListener(object : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }
    
    override fun onAnimationEnd(animation: Animation) {
      block(animation)
    }
    
    override fun onAnimationStart(animation: Animation?) {
    }
  })
}
