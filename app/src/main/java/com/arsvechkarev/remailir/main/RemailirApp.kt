package com.arsvechkarev.remailir.main

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class RemailirApp : Application() {
  
  override fun onCreate() {
    super.onCreate()
    val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(true)
      .build()
    FirebaseFirestore.getInstance().firestoreSettings = settings
  }
}