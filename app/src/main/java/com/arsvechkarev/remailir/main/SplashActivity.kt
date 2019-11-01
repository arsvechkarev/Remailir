package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (FirebaseAuth.getInstance().currentUser != null) {
      startActivity(Intent(this, CoreActivity::class.java))
    } else {
  
      startActivity(Intent(this, EntranceActivity::class.java))
    }
  }
}
