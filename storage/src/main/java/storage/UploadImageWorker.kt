package storage

import android.content.Context
import android.net.Uri
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.model.users.User
import core.model.users.withNewImageUri
import firebase.schema.Collections.Users
import firebase.utils.getProfilePhotoPath
import firebase.utils.thisUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import log.Loggable
import log.log
import javax.inject.Inject

class UploadImageWorker @Inject constructor(private val context: Context) {
  
  fun uploadImage(fileUri: String, user: User) {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()
    
    val params = Data.Builder().putAll(
      mapOf(
        "user_id" to user.id,
        "user_phone" to user.phone,
        "user_name" to user.name,
        "uri" to fileUri
      )
    ).build()
    
    val request = OneTimeWorkRequest.Builder(InternalUploadImageWorker::class.java)
      .setInputData(params)
      .setConstraints(constraints)
      .build()
    
    WorkManager.getInstance(context).enqueue(request)
  }
  
  class InternalUploadImageWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : RxWorker(context, workerParameters), Loggable {
    
    override fun createWork(): Single<Result> {
      val create = Single.create<Result> { emitter ->
        
        val userId = inputData.getString("user_id")!!
        val userPhone = inputData.getString("user_phone")!!
        val userName = inputData.getString("user_name")!!
        val imageUri = inputData.getString("uri")!!
        
        val fileUri = Uri.parse(imageUri)
        
        val partialUser = User(userId, userPhone, userName, "")
        
        log { "==========" }
        log { "got bytes" }
        
        val storageReference = FirebaseStorage.getInstance().reference.child(getProfilePhotoPath())
        
        val task1 = storageReference.putFile(fileUri)
        log { "start putting bytes" }
        storageReference.downloadUrl.addOnSuccessListener { uri ->
          log { "got url" }
          FirebaseFirestore.getInstance().collection(Users)
            .document(thisUser.uid)
            .set(partialUser.withNewImageUri(uri)).addOnSuccessListener {
              log { "set user" }
              task1.addOnSuccessListener {
                log { "task1 completed, all ok" }
                emitter.onSuccess(Result.success())
              }.addOnFailureListener {
                emitter.onError(it)
              }
            }
        }
      }
      return create.subscribeOn(Schedulers.io())
    }
    
    override val logTag: String
      get() = "UploadingImage"
    
  }
}