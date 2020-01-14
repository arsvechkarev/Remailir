package firebase.utils

import com.google.firebase.auth.FirebaseAuth
import core.model.users.User
import core.strings.UNDERSCORE
import firebase.schema.Storage
import java.util.UUID

fun randomUid(): String = UUID.randomUUID().toString()

fun getOtherUser(userOne: User, userTwo: User): User {
  if (userOne.id == FirebaseAuth.getInstance().uid!!) {
    return userTwo
  }
  if (userTwo.id == FirebaseAuth.getInstance().uid!!) {
    return userOne
  }
  throw IllegalStateException("Users ids don't match this user id")
}


fun calculateChatIdWith(
  firstUserId: String,
  secondUserId: String = FirebaseAuth.getInstance().uid!!
): String {
  return if (firstUserId < secondUserId) {
    firstUserId + UNDERSCORE + secondUserId
  } else {
    secondUserId + UNDERSCORE + firstUserId
  }
}

fun getProfilePhotoPath(userId: String) = Storage.images + "/" + userId + "_profile.jpg"
