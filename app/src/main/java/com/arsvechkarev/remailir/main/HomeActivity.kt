package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arsvechkarev.core.base.CoreActivity
import com.arsvechkarev.core.extensions.switchFragment
import com.arsvechkarev.messages.presentation.MessagesFragment
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.remailir.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.bottomNavigationBar

class HomeActivity : AppCompatActivity(), CoreActivity {
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    switchFragment(R.id.baseContainer, MessagesFragment())
    bottomNavigationBar.selectedItemId = R.id.itemMessages
    bottomNavigationBar.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.itemMessages -> switchFragment(R.id.baseContainer, MessagesFragment())
        R.id.itemProfile -> switchFragment(R.id.baseContainer, ProfileFragment())
      }
      return@setOnNavigationItemSelectedListener true
    }
  }
  
  override fun goToFragmentFromRoot(fragment: Fragment, addToBackStack: Boolean) {
    switchFragment(android.R.id.content, fragment, addToBackStack)
  }
  
  override fun signOut() {
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(this, EntranceActivity::class.java)
    startActivity(intent)
  }
  
}
