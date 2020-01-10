package firebase.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import core.strings.UNDERSCORE
import java.util.UUID

fun randomUid(): String = UUID.randomUUID().toString()

val thisUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

fun getChatIdWith(otherUserId: String): String {
  val thisUserId = FirebaseAuth.getInstance().uid!!
  return if (otherUserId < thisUserId) {
    otherUserId + UNDERSCORE + thisUserId
  } else {
    thisUserId + UNDERSCORE + otherUserId
  }
}
