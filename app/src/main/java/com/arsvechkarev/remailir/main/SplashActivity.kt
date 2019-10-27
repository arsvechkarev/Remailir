package com.arsvechkarev.remailir.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (FirebaseAuth.getInstance().currentUser != null) {
      startActivity(Intent(this, HomeActivity::class.java))
    } else {
      startActivity(Intent(this, EntranceActivity::class.java))
    }
  }
}
