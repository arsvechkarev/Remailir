package core.ui.utils

import android.view.View
import viewdsl.makeInvisible
import viewdsl.makeVisible

inline fun <T : Any> T.ifTrue(condition: (T) -> Boolean, block: T.() -> Unit) {
  if (condition(this)) apply(block)
}

fun makeViewsInvisibleExcept(shouldAnimate: Boolean, allViews: Array<View>, except: View) {
  allViews.forEach { view ->
    if (view === except) {
      view.makeVisible(shouldAnimate)
    } else {
      view.makeInvisible(shouldAnimate)
    }
  }
}