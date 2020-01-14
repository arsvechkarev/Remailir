package storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import core.model.users.User
import firebase.schema.ChatModel.memberIds
import firebase.schema.ChatModel.userOne
import firebase.schema.ChatModel.userTwo
import firebase.schema.Collections.OneToOneChats
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
    val imageLocalUrlParams = workersMap(imageLocalPath to filePath)
  
    val getLinkWorker = networkWorkerOf<InternalGetLinkWorker>(imageLocalUrlParams)
    val updateOneUsersRequest = networkWorkerOf<InternalUserUpdateWorker>()
    val chatsUrlUpdateWorker = networkWorkerOf<InternalImageUrlThroughChatsUpdateWorker>()
    
    WorkManager.getInstance(context)
      .beginWith(getLinkWorker)
      .then(listOf(updateOneUsersRequest, chatsUrlUpdateWorker))
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
  
  class InternalImageUrlThroughChatsUpdateWorker(
    context: Context,
    workerParameters: WorkerParameters
  ) : Worker(context, workerParameters), Loggable {
    
    override val logTag = "Profile_Upload_UpdateUrlInChats"
    
    override fun doWork(): Result {
      val latch = CountDownLatch(1)
      log { "start update user" }
      
      val thisUserId = FirebaseAuth.getInstance().uid!!
      
      FirebaseStorage.getInstance().reference.child(getProfilePhotoPath(AppUser.get().id))
        .downloadUrl.addOnSuccessListener { uri ->
        FirebaseFirestore.getInstance().collection(OneToOneChats)
          .whereArrayContains(memberIds, thisUserId)
          .get()
          .addOnSuccessListener { snapshot ->
            snapshot.documents.forEach {
              log { "Chat id = ${it.id}" }
              val userOneObject =
                it.get(userOne, User::class.java).also { log { "u1 = $it" } } as User
              val userTwoObject =
                it.get(userTwo, User::class.java).also { log { "u2 = $it" } } as User
              log { "user one = $userOneObject" }
              log { "user two = $userTwoObject" }
              FirebaseFirestore.getInstance().collection(OneToOneChats)
                .document(it.id)
                .update(
                  updateCorrectImageUri(thisUserId, uri.toString(), userOneObject, userTwoObject)
                ).addOnSuccessListener {
                  log { "Updated chats successfully" }
                  latch.countDown()
                }.addOnFailureListener {
                  log { "Error while updating chats" }
                  latch.countDown()
                }
            }
          }.addOnFailureListener {
            log(it) { "Error while getting documents" }
            latch.countDown()
          }
      }.addOnFailureListener {
        latch.countDown()
        log(it) { "Updating chats error while getting reference" }
      }
      
      latch.await()
      return Result.success()
    }
    
    private fun updateCorrectImageUri(
      thisUserId: String,
      imageUri: String,
      userOneObject: User,
      userTwoObject: User
    ): Map<String, String> {
      val map: Map<String, String> = when (thisUserId) {
        userOneObject.id -> hashMapOf("$userOne.$imageUrl" to imageUri)
        userTwoObject.id -> hashMapOf("$userTwo.$imageUrl" to imageUri)
        else -> throw IllegalStateException("This user id does't match either of users ids in a chat")
      }
      return map.also { log { "update map = $it" } }
    }
  }
  
  companion object {
    private const val imageLocalPath = "imageLocalPath"
  }
}