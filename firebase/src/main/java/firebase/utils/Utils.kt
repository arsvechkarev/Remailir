package firebase.utils

import com.google.firebase.auth.FirebaseAuth
import core.strings.UNDERSCORE
import firebase.schema.Storage
import java.util.UUID

fun randomUid(): String = UUID.randomUUID().toString()

fun calculateChatIdWith(otherUserId: String): String {
  val thisUserId = FirebaseAuth.getInstance().uid!!
  return if (otherUserId < thisUserId) {
    otherUserId + UNDERSCORE + thisUserId
  } else {
    thisUserId + UNDERSCORE + otherUserId
  }
}

fun getProfilePhotoPath(userId: String) = Storage.images + "/" + userId + "_profile.jpg"
