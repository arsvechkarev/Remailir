package com.arsvechkarev.core.extensions

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.switchFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  addToBackStack: Boolean = false
) {
  val transaction = supportFragmentManager.beginTransaction()
    .replace(containerIdRes, fragment)
  if (addToBackStack) transaction.addToBackStack(null)
  transaction.commit()
}

fun AppCompatActivity.switchFragment(
  @IdRes containerIdRes: Int,
  fragment: Fragment,
  runOnCommit: () -> Unit
) {
  val transaction = supportFragmentManager.beginTransaction()
    .replace(containerIdRes, fragment)
  transaction.runOnCommit(runOnCommit)
  transaction.commit()
}
