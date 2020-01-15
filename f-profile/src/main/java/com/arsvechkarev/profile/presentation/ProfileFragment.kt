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
import com.arsvechkarev.styles.COLOR_PROGRESS_CIRCLE
import com.arsvechkarev.styles.COLOR_PROGRESS_CIRCLE_BG
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
import com.theartofdev.edmodo.cropper.CropImageView
import core.MaybeResult
import core.base.BaseFragment
import core.di.coreComponent
import core.di.modules.ContextModule
import core.extensions.invisible
import core.extensions.observe
import core.extensions.showToast
import core.extensions.viewModelOf
import core.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.imageEditProfilePhoto
import kotlinx.android.synthetic.main.fragment_profile.imageProfile
import kotlinx.android.synthetic.main.fragment_profile.progressBarImageLoading
import kotlinx.android.synthetic.main.fragment_profile.swipeToRefreshLayoutProfile
import kotlinx.android.synthetic.main.fragment_profile.textProfileName
import kotlinx.android.synthetic.main.fragment_profile.textProfilePhone
import log.Loggable
import log.log
import storage.AppUser
import javax.inject.Inject


class ProfileFragment : BaseFragment(), Loggable {
  
  override val logTag = "ProfileStuff"
  
  override val layout: Int = R.layout.fragment_profile
  
  private val permissionDelegate = PermissionDelegate(this)
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ProfileViewModel
  
  override fun onInit() {
    swipeToRefreshLayoutProfile.setColorSchemeColors(COLOR_PROGRESS_CIRCLE)
    swipeToRefreshLayoutProfile.setProgressBackgroundColorSchemeColor(COLOR_PROGRESS_CIRCLE_BG)
    DaggerProfileComponent.builder()
      .coreComponent(coreComponent)
      .contextModule(ContextModule(context!!))
      .build()
      .inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(profileImageState, ::updateProfileImage)
    }
    swipeToRefreshLayoutProfile.setOnRefreshListener {
      viewModel.fetchImageFromNetwork(AppUser.get().imageUrl)
    }
    setUserInfo()
    if (permissionDelegate.allowReadAndWriteExternalStorage) {
      startImageLoading()
    } else {
      permissionDelegate.requestReadAndWriteExternalStorage()
    }
    imageEditProfilePhoto.setOnClickListener {
      startActivityForResult(Intent(ACTION_PICK, EXTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST)
    }
  }
  
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
  ) {
    if (permissionDelegate.allowReadAndWriteExternalStorage) {
      startImageLoading()
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
          log { "uri = $resultUri" }
          val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, resultUri)
          imageProfile.setImageBitmap(bitmap)
          viewModel.uploadImage(bitmap)
        }
      }
    }
  }
  
  private fun startImageLoading() {
    viewModel.fetchProfileImage(AppUser.get().imageUrl)
    progressBarImageLoading.visible()
  }
  
  private fun setUserInfo() {
    textProfileName.text = AppUser.get().name
    textProfilePhone.text = AppUser.get().phone
  }
  
  private fun updateProfileImage(result: MaybeResult<Bitmap>) {
    swipeToRefreshLayoutProfile.isRefreshing = false
    progressBarImageLoading.invisible()
    when {
      result.isSuccess -> {
        imageProfile.setImageBitmap(result.data)
      }
      result.isFailure -> {
        showToast("Network error")
        imageProfile.setBackgroundResource(R.drawable.image_profile_stub)
      }
      result.isNothing -> {
        imageProfile.setBackgroundResource(R.drawable.image_profile_stub)
      }
    }
  }
  
  companion object {
    
    const val PICK_IMAGE_REQUEST = 654
  }
}
