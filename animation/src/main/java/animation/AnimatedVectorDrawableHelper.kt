package animation

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.os.postDelayed


/**
 * Animates vector toggle (handles drawable to animate to automatically)
 */
fun ImageView.animateVectorToggle(
  toggle: VectorAnimationToggle, reversed: Boolean = false
) {
  val drawableDest = if (reversed) {
    toggle.getDrawableFrom(context)
  } else {
    toggle.getDrawableTo(context)
  }
  this.animateVectorDrawable()
  handler.postDelayed(toggle.duration) {
    setImageDrawable(drawableDest)
  }
}

enum class VectorAnimationToggle(
  @DrawableRes val drawableResIdFrom: Int,
  val labelFrom: String,
  @DrawableRes val drawableResIdTo: Int,
  val labelTo: String,
  val duration: Long
) {
  
  
  /**
   * Animates search icon to close icon and backwards
   */
  SEARCH_AND_CLOSE(
    R.drawable.avd_search_to_close, "to close",
    R.drawable.avd_close_to_search, "to search",
    VECTOR_DEFAULT_DURATION
  );
  
  fun getDrawableFrom(context: Context): Drawable = context.getDrawable(drawableResIdFrom)!!
  fun getDrawableTo(context: Context): Drawable = context.getDrawable(drawableResIdTo)!!
  
}


