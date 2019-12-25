package core.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.switchFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val transaction = supportFragmentManager.beginTransaction()
    .add(containerIdRes, fragment, Fragment::class.java.simpleName)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}

