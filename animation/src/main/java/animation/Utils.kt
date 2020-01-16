package animation

import android.animation.Animator
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes


fun ViewPropertyAnimator.duration(duration: Long): ViewPropertyAnimator {
  this.duration = duration
  return this
}

fun ViewPropertyAnimator.interpolator(interpolator: TimeInterpolator): ViewPropertyAnimator {
  this.interpolator = interpolator
  return this
}

fun View.loadAnimation(@AnimRes resId: Int): Animation {
  return AnimationUtils.loadAnimation(context, resId)
}

inline fun View.loadAnimationAnd(@AnimRes resId: Int, block: Animation.() -> Unit): Animation {
  val animation = AnimationUtils.loadAnimation(context, resId)
  animation.apply(block)
  return animation
}

inline fun ViewPropertyAnimator.onEnd(crossinline block: () -> Unit): ViewPropertyAnimator {
  setListener(object : Animator.AnimatorListener {
    
    override fun onAnimationEnd(animation: Animator) {
      block()
    }
    
    override fun onAnimationCancel(animation: Animator?) {}
    override fun onAnimationRepeat(animation: Animator) {}
    override fun onAnimationStart(animation: Animator?) {}
  })
  return this
}


inline fun Animation.doAfterAnimation(crossinline block: Animation.() -> Unit) {
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
