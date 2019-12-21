package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arsvechkarev.auth.presentation.fragments.PhoneFragment
import com.arsvechkarev.core.declaration.EntranceActivity
import com.arsvechkarev.core.extensions.switchFragment
import com.arsvechkarev.remailir.R
import com.arsvechkarev.signin.presentation.SignInFragment
import com.arsvechkarev.signup.presentation.SignUpFragment

class EntranceActivity : AppCompatActivity(), EntranceActivity {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_entrance)
    switchFragment(R.id.rootContainer, PhoneFragment(), true)
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
  
  override fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
    val transaction = supportFragmentManager.beginTransaction()
      .replace(R.id.rootContainer, fragment)
    if (addToBackStack) transaction.addToBackStack(null)
    transaction.commit()
  }
  
}
