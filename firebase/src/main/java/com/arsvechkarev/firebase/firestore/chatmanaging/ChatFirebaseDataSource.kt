package com.arsvechkarev.firebase.firestore.chatmanaging

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.firebase.firestore.FirestoreSchema.activeUserFieldName
import com.arsvechkarev.firebase.firestore.FirestoreSchema.active_prefix
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chatDocumentName
import com.arsvechkarev.firebase.firestore.FirestoreSchema.chats
import com.arsvechkarev.firebase.firestore.FirestoreSchema.participants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.withContext
import timber.log.Timber

class ChatFirebaseDataSource(
  private val thisUserUsername: String,
  private val dispatchers: Dispatchers
) : ChatRequestsDataSource, ChatMetaInfoDataSource {
  
  private val instance = FirebaseFirestore.getInstance()
  
  private var alreadyJoined = false
  private var registrationListener: ListenerRegistration? = null
  
  override suspend fun setCurrentUserAsActive(otherUserUsername: String) {
    createChatDocumentIfNotExists(otherUserUsername)
    instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .update(activeUserFieldName(thisUserUsername), true)
        .await()
  }
  
  override fun setCurrentUserAsInactive(otherUserUsername: String) {
    instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .update(activeUserFieldName(thisUserUsername), false)
        .addOnSuccessListener {
          Timber.d("Set current user \"$thisUserUsername\" as inactive")
        }
        .addOnFailureListener {
          Timber.d(it, "Failure setting current user \"$thisUserUsername\" as inactive")
        }
  }
  
  override suspend fun getCurrentlyWaitingForChat(): List<String> = withContext(dispatchers.IO) {
    val snapshot = instance.collection(chats)
        .whereArrayContains(participants, thisUserUsername)
        .get()
        .await() ?: return@withContext emptyList()
    val waitingForChatting = ArrayList<String>()
    for (snapshotElement in snapshot) {
      // Despite being nested loop, it will run only several
      // times just to check whether other user is active
      for ((key, value) in snapshotElement.data) {
        if (key.startsWith(active_prefix) && !key.endsWith(thisUserUsername)) {
          if (value as Boolean) {
            waitingForChatting.add(key.drop(active_prefix.length))
            continue
          }
        }
      }
    }
    return@withContext waitingForChatting
  }
  
  override suspend fun respondToChatRequest(
    otherUserUsername: String
  ): Boolean = withContext(dispatchers.IO) lb@{
    val document = instance.collection(chats)
        .document(chatDocumentName(thisUserUsername, otherUserUsername))
        .get()
        .await()
    val isOtherActive = document!!.getBoolean(activeUserFieldName(otherUserUsername))!!
    if (isOtherActive) {
      val map = document.data ?: return@lb false
      map[activeUserFieldName(thisUserUsername)] = true
      instance.collection(chats)
          .document(chatDocumentName(thisUserUsername, otherUserUsername))
          .update(map)
          .await()
      return@lb true
    }
    return@lb false
  }
  
  override suspend fun waitForJoining(
    otherUserUsername: String,
    listener: ChatWaitingListener,
  ) = withContext(dispatchers.IO) {
    val chatDocumentName = chatDocumentName(thisUserUsername, otherUserUsername)
    createChatDocumentIfNotExists(otherUserUsername)
    registrationListener = instance.collection(chats)
        .document(chatDocumentName)
        .addSnapshotListener lb@{ snapshot, error ->
          if (error != null) {
            return@lb
          }
          val value = snapshot!!.getBoolean(activeUserFieldName(otherUserUsername))!!
          if (value) {
            listener.onUserBecameActive()
            alreadyJoined = true
          } else if (alreadyJoined) {
            listener.onUserBecameInactive()
          }
        }
  }
  
  private suspend fun createChatDocumentIfNotExists(otherUserUsername: String) {
    val chatDocumentName = chatDocumentName(thisUserUsername, otherUserUsername)
    val document = instance.collection(chats)
        .document(chatDocumentName)
        .get()
        .await()
    if (document == null || !document.exists()) {
      instance.collection(chats)
          .document(chatDocumentName)
          .set(mapOf<String, Any>(
            "$active_prefix$thisUserUsername" to false,
            "$active_prefix$otherUserUsername" to false,
            participants to arrayListOf(thisUserUsername, otherUserUsername)
          ))
          .await()
    }
  }
  
  override fun releaseJoiningListener() {
    registrationListener?.remove()
    registrationListener = null
  }
}