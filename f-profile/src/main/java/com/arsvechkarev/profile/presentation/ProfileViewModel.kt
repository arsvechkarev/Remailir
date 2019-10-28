package com.arsvechkarev.profile.presentation

import androidx.lifecycle.MutableLiveData
import com.arsvechkarev.core.base.BaseViewModel
import com.arsvechkarev.core.model.users.ThisUser
import com.arsvechkarev.profile.repositories.ProfileRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
  private val repository: ProfileRepository
) : BaseViewModel() {
  
  var userData = MutableLiveData<ThisUser>()
  
  fun fetchProfileData() {
    repository.fetchProfileData(onSuccess = {
      this@ProfileViewModel.userData.value = it.toObject(ThisUser::class.java)
    }, onFailure = {
    
    })
    //    repository.fetchProfileData(object : ValueEventListener {
    //      override fun onDataChange(snapshot: DataSnapshot) {
    //        Log.d("snapshot", snapshot.value.toString())
    //        this@ProfileViewModel.userData.value = snapshot.getValue(User::class.java) as User
    //      }
    //
    //      override fun onCancelled(p0: DatabaseError) {}
    //    })
  }
  
}