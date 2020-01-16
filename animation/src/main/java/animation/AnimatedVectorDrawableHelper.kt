package animation

import android.os.Handler
import android.widget.ImageView
import androidx.core.os.postDelayed


/**
 * Animates vector drawable and executes [block] after completion
 */
inline fun ImageView.animateAndThen(mode: VectorAnimationMode, crossinline block: () -> Unit) {
  val handler = Handler()
  this.animateVectorDrawable()
  handler.postDelayed(mode.duration) { block() }
}

enum class VectorAnimationMode(val duration: Long) {
  
  SEARCH_AND_CLOSE(VECTOR_DEFAULT_ANIMATION);
}


