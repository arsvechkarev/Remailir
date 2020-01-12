package com.arsvechkarev.remailir

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.remailir.entrance.EntranceActivity
import storage.AppUser

class SplashActivity : AppCompatActivity() {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (AppUser.isUserSaved(this)) {
      AppUser.retrieve(this)
      startActivity(Intent(this, CoreActivity::class.java))
    } else {
      startActivity(Intent(this, EntranceActivity::class.java))
    }
  }
}
