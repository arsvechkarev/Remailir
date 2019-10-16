package com.arsvechkarev.core.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

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

fun Fragment.goToFragment(@IdRes id: Int, fragment: Fragment, addToBackStack: Boolean = true) {
  val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction().replace(id, fragment)
  if (addToBackStack) fragmentTransaction.addToBackStack(null)
  fragmentTransaction.commit()
}