package animation

import android.view.View
import androidx.annotation.IdRes
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce

/**
 * An extension function which creates/retrieves a [SpringAnimation] and stores it in the [View]s
 * tag.
 */
fun View.spring(
  property: ViewProperty,
  stiffness: Float = 200f,
  damping: Float = 0.3f,
  startVelocity: Float? = null
): SpringAnimation {
  val key = getKey(property)
  var springAnim = getTag(key) as? SpringAnimation?
  if (springAnim == null) {
    springAnim = SpringAnimation(this, property).apply {
      spring = SpringForce().apply {
        this.dampingRatio = damping
        this.stiffness = stiffness
        startVelocity?.let { setStartVelocity(it) }
      }
    }
    setTag(key, springAnim)
  }
  return springAnim
}

/**
 * A class which adds [DynamicAnimation.OnAnimationEndListener]s to the given `springs` and invokes
 * `onEnd` when all have finished.
 */
class MultiSpringEndListener(
  onEnd: (Boolean) -> Unit,
  vararg springs: SpringAnimation
) {
  private val listeners = ArrayList<DynamicAnimation.OnAnimationEndListener>(springs.size)
  private var wasCancelled = false
  
  init {
    springs.forEach {
      val listener = object : DynamicAnimation.OnAnimationEndListener {
        override fun onAnimationEnd(
          animation: DynamicAnimation<out DynamicAnimation<*>>?,
          canceled: Boolean,
          value: Float,
          velocity: Float
        ) {
          animation?.removeEndListener(this)
          wasCancelled = wasCancelled or canceled
          listeners.remove(this)
          if (listeners.isEmpty()) {
            onEnd(wasCancelled)
          }
        }
      }
      it.addEndListener(listener)
      listeners.add(listener)
    }
  }
}

fun listenForAllSpringsEnd(
  vararg springs: SpringAnimation,
  onEnd: (Boolean) -> Unit
) = MultiSpringEndListener(onEnd, *springs)

/**
 * Map from a [ViewProperty] to an `id` suitable to use as a [View] tag.
 */
@IdRes
private fun getKey(property: ViewProperty): Int {
  return when (property) {
    SpringAnimation.TRANSLATION_X -> R.id.translation_x
    SpringAnimation.TRANSLATION_Y -> R.id.translation_y
    SpringAnimation.TRANSLATION_Z -> R.id.translation_z
    SpringAnimation.SCALE_X -> R.id.scale_x
    SpringAnimation.SCALE_Y -> R.id.scale_y
    SpringAnimation.ROTATION -> R.id.rotation
    SpringAnimation.ROTATION_X -> R.id.rotation_x
    SpringAnimation.ROTATION_Y -> R.id.rotation_y
    SpringAnimation.X -> R.id.x
    SpringAnimation.Y -> R.id.y
    SpringAnimation.Z -> R.id.z
    SpringAnimation.ALPHA -> R.id.alpha
    SpringAnimation.SCROLL_X -> R.id.scroll_x
    SpringAnimation.SCROLL_Y -> R.id.scroll_y
    else -> throw IllegalAccessException("Unknown ViewProperty: $property")
  }
}