package com.arsvechkarev.profile.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
import com.theartofdev.edmodo.cropper.CropImageView
import core.MaybeResult
import core.base.BaseFragment
import core.di.ContextModule
import core.util.observe
import core.util.showToast
import core.util.viewModelOf
import kotlinx.android.synthetic.main.fragment_profile.imageEdit
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.textProfilePhone
import storage.AppUser
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_profile
  
  private val permissionDelegate = PermissionDelegate(this)
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ProfileViewModel
  
  override fun onInit() {
    DaggerProfileComponent.builder()
      .contextModule(ContextModule(context!!))
      .build()
      .inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(profileImageState, ::updateProfileImage)
    }
    viewModel.fetchProfileImage(AppUser.get().imageUrl)
    setUserInfo()
    imageEdit.setOnClickListener {
      if (permissionDelegate.allowReadExternalStorage) {
        requestForImage()
      } else {
        permissionDelegate.requestReadExternalStorage()
      }
    }
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (permissionDelegate.allowReadExternalStorage) {
      requestForImage()
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      PICK_IMAGE_REQUEST -> {
        if (resultCode == RESULT_OK && data != null && data.data != null) {
          val uri = data.data!!
          CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(context!!, this)
        }
      }
      CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
        val result = CropImage.getActivityResult(data)
        if (resultCode == RESULT_OK) {
          val resultUri: Uri = result.uri
          val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, resultUri)
          imageProfile.setImageBitmap(bitmap)
          viewModel.uploadImageRx(resultUri.toString())
        } else if (resultCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
          val error = result.error
        }
      }
    }
  }
  
  private fun requestForImage() {
    startActivityForResult(Intent(ACTION_PICK, EXTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST)
  }
  
  private fun setUserInfo() {
    textProfileName.text = AppUser.get().name
    textProfilePhone.text = AppUser.get().phone
  }
  
  private fun updateProfileImage(result: MaybeResult<Bitmap>) {
    when {
      result.isSuccess -> imageProfile.setImageBitmap(result.data)
      result.isFailure -> showToast("Failed to load bitmap")
      result.isNothing -> imageProfile.setBackgroundResource(R.drawable.image_profile_stub)
    }
  }
  
  companion object {
    
    const val PICK_IMAGE_REQUEST = 654
  }
}
