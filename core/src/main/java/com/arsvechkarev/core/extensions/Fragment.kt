package com.arsvechkarev.core.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.setTitle(@StringRes stringResId: Int) {
  requireActivity().setTitle(stringResId)
}

fun Fragment.clearBackStack() {
  repeat(fragmentManager!!.backStackEntryCount) {
    fragmentManager!!.popBackStack()
  }
}

fun Fragment.showKeyboard() {
  val inputMethodManager =
    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
}

fun Fragment.hideKeyboard(editText: EditText) {
  val inputMethodManager =
    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
  inputMethodManager!!.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun Fragment.showToast(@StringRes resId: Int) {
  Toast.makeText(context, getString(resId), Toast.LENGTH_LONG).show()
}

fun Fragment.showToast(message: String) {
  Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun Fragment.goToFragment(
  @IdRes id: Int = android.R.id.content,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val fragmentTransaction = requireFragmentManager().beginTransaction()
  fragmentTransaction.replace(id, fragment)
  if (addToBackStack) fragmentTransaction.addToBackStack(null)
  fragmentTransaction.commit()
}

fun Fragment.goToFragmentFromParent(
  @IdRes id: Int = android.R.id.content,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val fragmentTransaction = childFragmentManager.beginTransaction()
  if (addToBackStack) fragmentTransaction.addToBackStack(null)
  fragmentTransaction.replace(id, fragment)
  fragmentTransaction.commit()
}

fun Fragment.addFragment(
  @IdRes id: Int = android.R.id.content,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val fragmentTransaction = (activity!!.supportFragmentManager).beginTransaction()
  if (addToBackStack) fragmentTransaction.addToBackStack(null)
  fragmentTransaction.add(id, fragment)
  fragmentTransaction.commit()
}