package com.arsvechkarev.profile.presentation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment

private const val READ_EXTERNAL_STORAGE_PERMISSION = 1

class PermissionDelegate(private val fragment: Fragment) {
  
  val allowReadExternalStorage: Boolean
    get() = checkSelfPermission(
      fragment.requireContext(),
      READ_EXTERNAL_STORAGE
    ) == PERMISSION_GRANTED
  
  fun requestReadExternalStorage() {
    fragment.requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION)
  }
}