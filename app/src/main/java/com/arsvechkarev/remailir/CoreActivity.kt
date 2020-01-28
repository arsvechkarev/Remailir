package com.arsvechkarev.remailir

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arsvechkarev.chats.presentation.ChatsFragment
import com.arsvechkarev.profile.presentation.ProfileFragment
import com.arsvechkarev.remailir.entrance.presentation.ActualEntranceActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import core.base.BaseActivity
import core.base.CoreActivity
import core.extensions.switchToFragment
import core.extensions.transaction
import kotlinx.android.synthetic.main.activity_home.bottomNavigationBar
import storage.AppUser

class CoreActivity : BaseActivity(), CoreActivity {
  
  private val chatsFragment = ChatsFragment()
  private val profileFragment = ProfileFragment()
  
  private var activeFragment: Fragment = chatsFragment
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.decorView.systemUiVisibility =
      (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(R.layout.activity_home)
    transaction {
      add(R.id.baseContainer, profileFragment)
      hide(profileFragment)
      add(R.id.baseContainer, chatsFragment)
    }
    bottomNavigationBar.selectedItemId = R.id.itemChats
    bottomNavigationBar.setOnNavigationItemSelectedListener {
      when (it.itemId) {
        R.id.itemChats -> {
          transaction {
            hide(activeFragment)
            show(chatsFragment)
          }
          activeFragment = chatsFragment
        }
        R.id.itemProfile -> {
          transaction {
            hide(activeFragment)
            show(profileFragment)
          }
          activeFragment = profileFragment
        }
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
  
  override fun goToFragment(fragment: Fragment, addToBackStack: Boolean, animate: Boolean) {
    switchToFragment(android.R.id.content, fragment, addToBackStack, animate)
  }
  
  override fun signOut() {
    coroutine {
      FirebaseFirestore.getInstance().clearPersistence()
      FirebaseAuth.getInstance().signOut()
      AppUser.clear(this@CoreActivity)
      val intent = Intent(this@CoreActivity, ActualEntranceActivity::class.java)
      startActivity(intent)
    }
  }
  
}
