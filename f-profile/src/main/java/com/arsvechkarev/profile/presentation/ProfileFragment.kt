package com.arsvechkarev.profile.presentation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.squareup.picasso.Picasso
import core.base.BaseFragment
import core.model.users.User
import core.strings.DEFAULT_IMG_URL
import core.util.observe
import core.util.viewModelOf
import core.util.visible
import kotlinx.android.synthetic.main.fragment_profile.imageEdit
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.textProfilePhone
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
  
  private val PICK_IMAGE_REQUEST = 314
  private val READ_EXTERNAL_STORAGE_PERMISSION = 5
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var profileViewModel: ProfileViewModel
  
  override val layout: Int = R.layout.fragment_profile
  
  override fun onInit() {
    DaggerProfileComponent.create().inject(this)
    profileViewModel = viewModelOf(viewModelFactory) {
      observe(userData, ::updateProfile)
    }
    profileViewModel.fetchProfileData()
    imageEdit.setOnClickListener {
      if (checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity!!.startActivityForResult(intent, PICK_IMAGE_REQUEST)
      } else {
        requestPermissions(arrayOf(READ_EXTERNAL_STORAGE), READ_EXTERNAL_STORAGE_PERMISSION)
      }
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if (requestCode == PICK_IMAGE_REQUEST) {
      if (resultCode == RESULT_OK && data != null && data.data != null) {
        val imageUri: Uri = data.data!!
        Log.d("qwerty", imageUri.toString())
        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, imageUri)
        imageProfile.setImageBitmap(bitmap)
      }
    }
  }
  
  private fun updateProfile(result: Result<User>) {
    result.onSuccess { user ->
      imageEdit.visible()
      if (user.imageUrl == DEFAULT_IMG_URL) {
        imageProfile.setBackgroundResource(R.drawable.image_profile_stub)
      } else {
        Picasso.get().load(user.imageUrl).into(imageProfile)
      }
      textProfileName.text = user.name
      textProfilePhone.text = user.phone
    }
  }
}
