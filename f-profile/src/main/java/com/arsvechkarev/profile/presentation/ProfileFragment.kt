package com.arsvechkarev.profile.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.net.Uri
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.arsvechkarev.views.LoadingDialog
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
import com.theartofdev.edmodo.cropper.CropImageView
import core.RawResult
import core.base.BaseFragment
import core.model.users.User
import core.onFailure
import core.onSuccess
import core.strings.DEFAULT_IMG_URL
import core.util.observe
import core.util.showToast
import core.util.viewModelOf
import core.util.visible
import kotlinx.android.synthetic.main.fragment_profile.imageEdit
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.textProfilePhone
import log.debug
import javax.inject.Inject


class ProfileFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_profile
  
  private val permissionDelegate = PermissionDelegate(this)
  private var loadingDialog: LoadingDialog? = null
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ProfileViewModel
  
  override fun onInit() {
    DaggerProfileComponent.create().inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(userDataState, ::updateProfile)
      observe(uploadingImageState, ::handleUpload)
    }
    viewModel.fetchProfileDataRx()
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
          viewModel.uploadImageRx(bitmap)
          loadingDialog = LoadingDialog.create("Uploading image")
          loadingDialog!!.show(childFragmentManager, null)
        } else if (resultCode == CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
          val error = result.error
        }
      }
    }
  }
  
  private fun requestForImage() {
    startActivityForResult(Intent(ACTION_PICK, EXTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST)
  }
  
  private fun updateProfile(result: Result<User>) {
    result.fold({ user ->
      imageEdit.visible()
      if (user.imageUrl == DEFAULT_IMG_URL) {
        imageProfile.setBackgroundResource(R.drawable.image_profile_stub)
      } else {
        Picasso.get().load(user.imageUrl).into(imageProfile)
      }
      textProfileName.text = user.name
      textProfilePhone.text = user.phone
    }, {
      showToast("Failed to load user info")
    })
  }
  
  private fun handleUpload(rawResult: RawResult) {
    rawResult.apply {
      onSuccess {
        loadingDialog?.dismiss()
        showToast("Successfully uploaded")
      }
      onFailure {
        showToast("Failure while uploading the photo")
        debug(it) { "Exception." }
      }
    }
  }
  
  companion object {
    
    const val PICK_IMAGE_REQUEST = 654
  }
}
