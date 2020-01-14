package com.arsvechkarev.remailir

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arsvechkarev.chats.presentation.ChatsFragment
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.remailir.entrance.presentation.EntranceActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.base.BaseActivity
import core.base.CoreActivity
import core.extensions.switchFragment
import kotlinx.android.synthetic.main.activity_home.bottomNavigationBar
import storage.AppUser

class CoreActivity : BaseActivity(), CoreActivity {
  
  private val messagesFragment = ChatsFragment()
  private val profileFragment = ProfileFragment()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_home)
    switchFragment(R.id.baseContainer, ChatsFragment())
    bottomNavigationBar.selectedItemId = R.id.itemMessages
    bottomNavigationBar.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.itemMessages -> switchFragment(R.id.baseContainer, messagesFragment)
        R.id.itemProfile -> switchFragment(R.id.baseContainer, profileFragment)
      }
      return@setOnNavigationItemSelectedListener true
    }
  }
  
  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (profileFragment.isVisible) {
      profileFragment.onActivityResult(requestCode, resultCode, data)
    }
  }
  
  override fun goToFragmentFromRoot(fragment: Fragment, addToBackStack: Boolean) {
    switchFragment(android.R.id.content, fragment, addToBackStack)
  }
  
  override fun signOut() {
    coroutine {
      FirebaseFirestore.getInstance().clearPersistence()
      FirebaseAuth.getInstance().signOut()
      AppUser.clear(this@CoreActivity)
      val intent = Intent(this@CoreActivity, EntranceActivity::class.java)
      startActivity(intent)
    }
  }
  
}
