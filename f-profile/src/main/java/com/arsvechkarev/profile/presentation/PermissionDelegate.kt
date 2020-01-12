package com.arsvechkarev.profile.presentation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment

private const val READ_AND_WRITE_EXTERNAL_STORAGE = 1

class PermissionDelegate(private val fragment: Fragment) {
  
  val allowReadAndWriteExternalStorage: Boolean
    get() {
      val readStorage = checkSelfPermission(fragment.requireContext(), READ_EXTERNAL_STORAGE)
      val writeStorage = checkSelfPermission(fragment.requireContext(), WRITE_EXTERNAL_STORAGE)
      return readStorage == PERMISSION_GRANTED && writeStorage == PERMISSION_GRANTED
    }
  
  fun requestReadAndWriteExternalStorage() {
    fragment.requestPermissions(
      arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
      READ_AND_WRITE_EXTERNAL_STORAGE
    )
  }
}