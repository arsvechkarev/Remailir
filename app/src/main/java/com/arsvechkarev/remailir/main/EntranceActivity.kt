package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.core.declaration.EntranceActivity
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
    val intent = Intent(this, CoreActivity::class.java)
    startActivity(intent)
  }
  
  override fun goToSignIn() {
    switchFragment(R.id.rootContainer, SignInFragment(), true)
  }
  
  override fun goToSighUp() {
    supportFragmentManager.popBackStack()
    switchFragment(R.id.rootContainer, SignUpFragment())
  }
  
}
