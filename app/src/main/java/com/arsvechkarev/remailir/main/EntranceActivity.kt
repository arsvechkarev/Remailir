package com.arsvechkarev.remailir.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.arsvechkarev.auth.presentation.fragments.PhoneFragment
import com.arsvechkarev.core.declaration.EntranceActivity
import com.arsvechkarev.core.extensions.switchFragment
import com.arsvechkarev.core.model.Country
import com.arsvechkarev.remailir.R

class EntranceActivity : AppCompatActivity(), EntranceActivity {
  private val phoneFragment = PhoneFragment()
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_entrance)
    switchFragment(R.id.rootContainer, phoneFragment, false)
  }
  
  override fun goToBase() {
    val intent = Intent(this, CoreActivity::class.java)
    startActivity(intent)
  }
  
  override fun onCountrySelected(country: Country) {
    phoneFragment.onCountrySelected(country)
  }
  
  override fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
    val transaction = supportFragmentManager.beginTransaction()
      .replace(R.id.rootContainer, fragment)
    if (addToBackStack) transaction.addToBackStack(null)
    transaction.commit()
  }
  
}
