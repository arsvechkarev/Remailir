package animation

import android.graphics.drawable.AnimatedVectorDrawable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

fun View.fadeOut() {
  this.animate()
    .alpha(0f)
    .duration(FADE_DEFAULT_DURATION)
    .start()
}

inline fun View.scaleDown(crossinline onEndBlock: () -> Unit = {}) {
  this.animate()
    .scaleX(0f)
    .scaleY(0f)
    .onEnd(onEndBlock)
    .duration(FADE_DEFAULT_DURATION)
    .start()
}

fun View.rotateOnce() {
  this.animate()
    .rotationBy(360f)
    .duration(VECTOR_SHORT_DURATION)
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
