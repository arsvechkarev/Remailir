package com.arsvechkarev.remailir.registration.tests

import android.os.SystemClock
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import core.model.users.NewUser
import firebase.Collections
import org.junit.ClassRule
import org.junit.rules.ExternalResource
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(BaseTest::class)
class RegistrationSuites {
  
  companion object {
    
    private val TAG = "FirebaseTestSuite"
    
    @get:ClassRule
    @JvmStatic
    val resource: ExternalResource = object : ExternalResource() {
      
      @Throws(Throwable::class)
      override fun before() {
        // Printing to Log.ERROR because it's more noticeable
        Log.e(TAG, "Started to requesting firebase")
        FirebaseFirestore.getInstance().collection(Collections.Users)
          .whereEqualTo("phone", "+15555551234")
          .get()
          .addOnCompleteListener { task ->
            val objects = task.result?.toObjects(NewUser::class.java)
            when {
              objects.isNullOrEmpty() -> {
                Log.e(TAG, "No test account found in the database")
              }
              objects.size > 1 -> {
                Log.e(TAG, "Found multiple accounts in the database:")
                objects.forEach {
                  Log.e(TAG, it.toString())
                }
              }
              else -> {
                Log.e(TAG, "Found exactly one test account in the database: ${objects[0]}")
                val id = objects[0].id
                FirebaseFirestore.getInstance().collection(Collections.Users)
                  .document(id)
                  .delete()
                  .addOnSuccessListener {
                    Log.e(TAG, "Test account successfully wiped out from the database")
                  }
              }
            }
          }
        SystemClock.sleep(5000)
      }
      
      override fun after() {
      }
    }
  }
}