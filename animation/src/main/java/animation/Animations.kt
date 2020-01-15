package animation

import android.animation.AnimatorInflater
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.widget.ImageView
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun View.rotateInfinitely() {
  val animator = AnimatorInflater.loadAnimator(context, R.animator.infinite_rotation)
  animator.setTarget(this)
  animator.start()
}

/**
 * Animates vector drawable taken from background
 */
fun ImageView.animateVectorDrawable() {
  when (val vectorDrawable = drawable) {
    is AnimatedVectorDrawable -> vectorDrawable.start()
    is AnimatedVectorDrawableCompat -> vectorDrawable.start()
    else -> throw IllegalArgumentException("Background of the image is not a vector drawable!")
  }
}
