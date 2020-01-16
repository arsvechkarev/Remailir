package animation

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView


fun View.rotateOnce() {
  this.animate()
    .rotationBy(360f)
    .duration(VECTOR_SMALL_ANIMATION)
    .interpolator(AccelerateDecelerateInterpolator())
    .start()
}

/**
 * Animates vector drawable taken from background
 */
fun ImageView.animateVectorDrawable(): AnimatedVectorDrawable {
  return when (val vectorDrawable = drawable) {
    is AnimatedVectorDrawable -> vectorDrawable.also { it.start() }
    else -> throw IllegalArgumentException("Background of the image is not a vector drawable!")
  }
}
