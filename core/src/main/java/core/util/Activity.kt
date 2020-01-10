package core.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.switchFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val transaction = supportFragmentManager.beginTransaction()
    .replace(containerIdRes, fragment, Fragment::class.java.simpleName)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}

fun Context.showToast(text: CharSequence) {
  Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}
