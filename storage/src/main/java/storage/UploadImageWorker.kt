package storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import firebase.schema.Collections.Users
import firebase.schema.UserModel.imageUrl
import firebase.utils.getProfilePhotoPath
import log.Loggable
import log.log
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.CountDownLatch
import javax.inject.Inject


class UploadImageWorker @Inject constructor(private val context: Context) {
  
  fun uploadImage(filePath: String) {
    val constraints = Constraints.Builder()
      .setRequiredNetworkType(NetworkType.CONNECTED)
      .build()
    
    val imageLocalUrlParams = workersMap(imageLocalPath to filePath)
    
    val getLinkWorker = OneTimeWorkRequest.Builder(InternalGetLinkWorker::class.java)
      .setInputData(imageLocalUrlParams)
      .setConstraints(constraints)
      .build()
  
    val workRequest = OneTimeWorkRequest.Builder(InternalUserUpdateWorker::class.java)
      .setConstraints(constraints)
      .build()
    
    WorkManager.getInstance(context)
      .beginWith(getLinkWorker)
      .then(workRequest)
      .enqueue()
  }
  
  internal class InternalGetLinkWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : Worker(context, workerParameters), Loggable {
  
    override val logTag = "Profile_Upload_GetLink"
    
    override fun doWork(): Result {
      val latch = CountDownLatch(1)
      
      val storageReference =
        FirebaseStorage.getInstance().reference.child(getProfilePhotoPath(AppUser.get().id))
      val localFilePath = inputData.getString(imageLocalPath)!!
      val bitmap = BitmapFactory.decodeFile(localFilePath)
      
      val file = File(localFilePath)
      FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, it)
      }
      
      storageReference.putBytes(file.readBytes())
        .addOnSuccessListener {
          storageReference.downloadUrl.addOnSuccessListener { uri ->
            log { "uri = $uri" }
            log { "uploaded image" }
            latch.countDown()
          }.addOnFailureListener {
            log { "getting uri error" }
            latch.countDown()
          }
        }.addOnFailureListener {
          latch.countDown()
          log { "updloaded image error" }
        }
      
      latch.await()
      return Result.success()
    }
  }
  
  internal class InternalUserUpdateWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : Worker(context, workerParameters), Loggable {
  
    override val logTag = "Profile_Upload_UpdateUser"
    
    override fun doWork(): Result {
      val latch = CountDownLatch(1)
      log { "start update user" }
  
      FirebaseStorage.getInstance().reference.child(getProfilePhotoPath(AppUser.get().id))
        .downloadUrl.addOnSuccessListener { uri ->
        FirebaseFirestore.getInstance().collection(Users)
          .document(AppUser.get().id)
          .update(imageUrl, uri.toString())
          .addOnSuccessListener {
            latch.countDown()
            log { "updated user" }
          }
          .addOnFailureListener {
            latch.countDown()
            log(it) { "updated user error 1" }
          }
      }.addOnFailureListener {
        latch.countDown()
        log(it) { "updated user error 2" }
      }
      
      latch.await()
      return Result.success()
    }
  }
  
  companion object {
    private const val imageLocalPath = "imageLocalPath"
  }
}