package com.arsvechkarev.core.extensions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.switchToFragment(fragment: Fragment, addToBackStack: Boolean = false) {
  val transaction = supportFragmentManager.beginTransaction()
    .replace(android.R.id.content, fragment)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}