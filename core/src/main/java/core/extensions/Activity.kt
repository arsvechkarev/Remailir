package core.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import core.R

fun AppCompatActivity.switchFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  addToBackStack: Boolean = false,
  animate: Boolean = true
) {
  val transaction = supportFragmentManager.beginTransaction()
  if (animate) {
    transaction.setCustomAnimations(
      R.anim.transition_fragment_enter1,
      R.anim.transition_fragment_exit1,
      R.anim.transition_fragment_pop_enter1,
      R.anim.transition_fragment_pop_exit1
    )
  }
  transaction.replace(containerIdRes, fragment)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}

fun AppCompatActivity.goToFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val transaction = supportFragmentManager.beginTransaction()
    .setCustomAnimations(
      R.anim.transition_fragment_enter1,
      R.anim.transition_fragment_exit1,
      R.anim.transition_fragment_pop_enter1,
      R.anim.transition_fragment_exit1
    )
    .replace(containerIdRes, fragment)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}

fun Context.showToast(text: CharSequence) {
  Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
