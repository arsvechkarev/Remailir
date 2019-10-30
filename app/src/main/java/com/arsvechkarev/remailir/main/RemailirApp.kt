package com.arsvechkarev.remailir.main

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.jakewharton.threetenabp.AndroidThreeTen

class RemailirApp : Application() {
  
  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    val settings = FirebaseFirestoreSettings.Builder()
      .setPersistenceEnabled(true)
      .build()
    FirebaseFirestore.getInstance().firestoreSettings = settings
  }
}