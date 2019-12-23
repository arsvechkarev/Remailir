package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.remailir.main.registration.EntranceActivity
import storage.Database

class SplashActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (Database.getUser(this) != null) {
      startActivity(Intent(this, CoreActivity::class.java))
    } else {
      startActivity(Intent(this, EntranceActivity::class.java))
    }
  }
}
