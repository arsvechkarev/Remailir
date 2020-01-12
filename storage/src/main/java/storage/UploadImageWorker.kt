package storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.RxWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import firebase.schema.Collections.Users
import firebase.schema.UserModel.imageUrl
import firebase.utils.getProfilePhotoPath
import firebase.utils.thisUser
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import log.Loggable
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

private const val imageStorageUrl = "imageStorageUrl"
private const val imageLocalPath = "imageLocalPath"

class UploadImageWorker @Inject constructor(private val context: Context) : Loggable {
  
  override val logTag = "LoadingStuff"
  
  fun uploadImage(filePath: String) {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()
    
    val imageLocalUrlParams = workersMap(imageLocalPath to filePath)
    
    val getLinkWorker = OneTimeWorkRequest.Builder(InternalGetLinkWorker::class.java)
      .setConstraints(constraints)
      .build()
    
    val updateUserWorker = OneTimeWorkRequest.Builder(InternalUpdateUserWorker::class.java)
      .setConstraints(constraints)
      .build()
    
    val uploadImageWorker = OneTimeWorkRequest.Builder(InternalUploadingImageWorker::class.java)
      .setConstraints(constraints)
      .setInputData(imageLocalUrlParams)
      .build()
    
    WorkManager.getInstance(context)
      .beginWith(getLinkWorker)
      .then(listOf(updateUserWorker, uploadImageWorker))
      .enqueue()
  }
  
  class InternalGetLinkWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : RxWorker(context, workerParameters) {
    
    override fun createWork(): Single<Result> {
      return Single.create<Result> { emitter ->
        val storageReference = FirebaseStorage.getInstance().reference.child(getProfilePhotoPath())
        storageReference.downloadUrl.addOnSuccessListener {
          debugg("uri = $it")
          emitter.onSuccess(Result.success(workersMap(imageStorageUrl to it)))
        }.addOnFailureListener {
          emitter.onError(it)
        }
      }.subscribeOn(Schedulers.io())
    }
  }
  
  class InternalUpdateUserWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : RxWorker(context, workerParameters) {
    
    override fun createWork(): Single<Result> {
      return Single.create<Result> { emitter ->
        val newImageUrl = inputData.getString(imageStorageUrl)!!
        debugg("newImageUrl = $newImageUrl")
    
        FirebaseFirestore.getInstance().collection(Users)
          .document(thisUser.uid)
          .update(imageUrl, newImageUrl).addOnSuccessListener {
            emitter.onSuccess(Result.success())
          }.addOnFailureListener {
            emitter.onError(it)
          }
      }.subscribeOn(Schedulers.io())
    }
  }
  
  class InternalUploadingImageWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : RxWorker(context, workerParameters) {
    
    override fun createWork(): Single<Result> {
      return Single.create<Result> { emitter ->
        val localUrl = inputData.getString(imageLocalPath)!!
        debugg("loc = $localUrl")
        val bitmap = BitmapFactory.decodeFile(localUrl)
        
        val file = File(localUrl)
        FileOutputStream(file).use {
          bitmap.compress(Bitmap.CompressFormat.JPEG, 30, it)
        }
        FirebaseStorage.getInstance().reference.child(getProfilePhotoPath())
          .putBytes(file.readBytes())
          .addOnSuccessListener {
            debugg("done")
            emitter.onSuccess(Result.success())
          }.addOnFailureListener {
            emitter.onError(it)
          }
      }.subscribeOn(Schedulers.io())
    }
  }
  
  companion object {
    fun debugg(message: String) = Log.d("LoadingStuff", message)
  }
}