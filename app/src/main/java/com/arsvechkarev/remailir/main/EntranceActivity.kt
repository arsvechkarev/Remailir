package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.core.base.EntranceActivity
import com.arsvechkarev.core.extensions.switchFragment
import com.arsvechkarev.remailir.R
import com.arsvechkarev.signin.presentation.SignInFragment
import com.arsvechkarev.signup.presentation.SignUpFragment

class EntranceActivity : AppCompatActivity(), EntranceActivity {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_entrance)
    goToSighUp()
  }
  
  override fun goToBase() {
    val intent = Intent(this, HomeActivity::class.java)
    startActivity(intent)
  }
  
  override fun goToSignIn() {
    switchFragment(R.id.rootContainer, SignInFragment())
  }
  
  override fun goToSighUp() {
    switchFragment(R.id.rootContainer, SignUpFragment())
  }

}
