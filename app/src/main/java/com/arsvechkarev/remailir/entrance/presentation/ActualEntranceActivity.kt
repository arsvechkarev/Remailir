package com.arsvechkarev.remailir.entrance.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.common.EntranceViewModel
import com.arsvechkarev.auth.common.PhoneAuthState
import com.arsvechkarev.auth.common.PhoneAuthState.Failed
import com.arsvechkarev.auth.common.PhoneAuthState.OnCodeSent
import com.arsvechkarev.auth.common.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.auth.common.PhoneAuthState.UserNotExist
import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.presentation.phone.PhoneFragment
import com.arsvechkarev.auth.presentation.registration.RegistrationFragment
import com.arsvechkarev.auth.presentation.sms.SmsCodeFragment
import com.arsvechkarev.remailir.CoreActivity
import com.arsvechkarev.remailir.R
import com.arsvechkarev.remailir.entrance.di.DaggerEntranceComponent
import com.google.firebase.auth.PhoneAuthProvider
import core.base.EntranceActivity
import core.di.coreComponent
import core.extensions.observe
import core.extensions.switchToFragment
import core.extensions.viewModelOf
import core.model.other.Country
import log.log
import javax.inject.Inject

class ActualEntranceActivity : AppCompatActivity(), EntranceActivity {
  
  private val registrationFragment = RegistrationFragment()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  
  @Inject
  lateinit var phoneAuthProvider: PhoneAuthProvider
  
  private lateinit var viewModel: EntranceViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.decorView.systemUiVisibility =
      (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(R.layout.activity_entrance)
    DaggerEntranceComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    viewModel = viewModelOf(viewModelFactory) {
      observe(phoneState(), ::handleState)
    }
    if (savedInstanceState == null) {
      switchToFragment(R.id.rootContainer, PhoneFragment(), addToBackStack = false, animate = false)
    }
  }
  
  private fun handleState(state: PhoneAuthState) {
    when (state) {
      is OnCodeSent -> {
        log { "on code sent" }
        goToFragment(SmsCodeFragment())
      }
      is UserAlreadyExists -> {
        log { "on user already exists" }
        goToCore()
      }
      is UserNotExist -> {
        log { "on user not exist" }
        goToFragment(registrationFragment)
      }
      is Failed -> {
        log(state.exception) { "failed" }
      }
    }
  }
  
  override fun goToCore() {
    val intent = Intent(this, CoreActivity::class.java)
    startActivity(intent)
    finish()
  }
  
  override fun onCountrySelected(country: Country) {
    repeat(supportFragmentManager.backStackEntryCount) {
      supportFragmentManager.popBackStack()
    }
    goToFragment(PhoneFragment.create(country))
  }
  
  private fun goToFragment(fragment: Fragment) {
    switchToFragment(R.id.rootContainer, fragment)
    viewModel.setPending()
  }
  
  override fun onPhoneEntered(phoneNumber: String) {
    viewModel.verifyPhone(phoneNumber, this)
  }
  
  override fun onCheckCode(code: String) {
    viewModel.checkCode(code)
  }
  
  override fun goToCountriesList() {
    viewModel.setPending()
    switchToFragment(R.id.rootContainer, CountriesFragment(), true)
  }
  
}
