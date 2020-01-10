package com.arsvechkarev.profile.presentation

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.profile.R
import com.arsvechkarev.profile.di.DaggerProfileComponent
import com.squareup.picasso.Picasso
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
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var viewModel: ProfileViewModel
  
  override fun onInit() {
    DaggerProfileComponent.create().inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(userDataState, ::updateProfile)
      observe(uploadingImageState, ::handleUpload)
    }
    viewModel.fetchProfileData()
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
    if (requestCode == PICK_IMAGE_REQUEST) {
      if (resultCode == RESULT_OK && data != null && data.data != null) {
        val uri = data.data!!
        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
        imageProfile.setImageBitmap(bitmap)
        viewModel.uploadImage(uri)
      }
    }
  }
  
  private fun requestForImage() {
    startActivityForResult(Intent(ACTION_PICK, EXTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST)
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
  
  private fun handleUpload(rawResult: RawResult) {
    rawResult.apply {
      onSuccess {
        showToast("Successfully uploaded")
      }
      onFailure {
        showToast("Failure")
        debug(it) { "Exception." }
      }
    }
  }
  
  companion object {
    
    const val PICK_IMAGE_REQUEST = 654
  }
}
