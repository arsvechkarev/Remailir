package com.arsvechkarev.remailir.utils

import android.os.SystemClock
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.NewUser
import firebase.Collections
import org.junit.rules.ExternalResource

object FirebaseFirestoreTest {
  
  val externalResource = object : ExternalResource() {
    override fun before() = deleteTestAccount()
  }
  
  fun deleteTestAccount() {
    val tag = "FirebaseTestSuite"
    // Printing to Log.ERROR because it's more noticeable
    Log.e(tag, "Started to requesting firebase")
    FirebaseFirestore.getInstance().collection(Collections.Users)
      // TODO (1/4/2020): Remove hardcoded phone
      .whereEqualTo("phone", "+15555551234")
      .get()
      .addOnCompleteListener { task ->
        val objects = task.result?.toObjects(NewUser::class.java)
        when {
          objects.isNullOrEmpty() -> {
            Log.e(tag, "No test account found in the database")
          }
          objects.size > 1 -> {
            Log.e(tag, "Found multiple accounts in the database:")
            objects.forEach {
              Log.e(tag, it.toString())
            }
          }
          else -> {
            Log.e(tag, "Found exactly one test account in the database: ${objects[0]}")
            val id = objects[0].id
            FirebaseFirestore.getInstance().collection(Collections.Users)
              .document(id)
              .delete()
              .addOnSuccessListener {
                Log.e(tag, "Test account successfully wiped out from the database")
              }
          }
        }
      }
    SystemClock.sleep(5000)
  }
}
