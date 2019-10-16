package com.arsvechkarev.remailir.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.arsvechkarev.chat.presentation.createChatFragment
import com.arsvechkarev.core.MainActivity
import com.arsvechkarev.core.model.Friend
import com.arsvechkarev.core.extensions.switchToFragment
import com.arsvechkarev.remailir.R
import com.arsvechkarev.signin.presentation.SignInFragment
import com.arsvechkarev.signup.presentation.SignUpFragment
import com.google.firebase.auth.FirebaseAuth

class BaseActivity : AppCompatActivity(), MainActivity {
  private var isMenuVisible: Boolean = false
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    goToSighUp()
    if (FirebaseAuth.getInstance().currentUser != null) {
      goToBase()
    } else {
      goToSighUp()
    }
  }
  
  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    menu?.forEach { it.isVisible = isMenuVisible }
    return true
  }
  
  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item?.itemId) {
      R.id.itemSignOut -> signOut()
    }
    return super.onOptionsItemSelected(item)
  }
  
  override fun goToBase() {
    switchToFragment(BaseFragment())
    updateMenu(isVisible = true)
  }
  
  override fun goToChat(friend: Friend) {
    switchToFragment(createChatFragment(friend), addToBackStack = true)
  }
  
  override fun goToProfile() {
  }
  
  override fun goToMessages() {
  }
  
  override fun goToSignIn() {
    switchToFragment(SignInFragment())
  }
  
  override fun goToSighUp() {
    switchToFragment(SignUpFragment())
  }
  
  override fun signOut() {
    FirebaseAuth.getInstance().signOut()
    goToSighUp()
    updateMenu(isVisible = false)
  }
  
  private fun updateMenu(isVisible: Boolean) {
    isMenuVisible = isVisible
    invalidateOptionsMenu()
  }
}
