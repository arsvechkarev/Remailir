package com.arsvechkarev.remailir

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arsvechkarev.messages.presentation.MessagesFragment
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.remailir.entrance.EntranceActivity
import com.google.firebase.auth.FirebaseAuth
import core.base.BaseActivity
import core.base.CoreActivity
import core.extensions.switchFragment
import kotlinx.android.synthetic.main.activity_home.bottomNavigationBar
import storage.Database

class CoreActivity : BaseActivity(), CoreActivity {
  
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
    coroutine {
      FirebaseAuth.getInstance().signOut()
      Database.deleteAll(this@CoreActivity)
      val intent = Intent(this@CoreActivity, EntranceActivity::class.java)
      startActivity(intent)
    }
  }
  
}
