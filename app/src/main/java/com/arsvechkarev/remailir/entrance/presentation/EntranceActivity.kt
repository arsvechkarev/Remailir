package com.arsvechkarev.remailir.entrance.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.presentation.phone.PhoneFragment
import com.arsvechkarev.auth.presentation.signup.RegistrationFragment
import com.arsvechkarev.auth.presentation.sms.SmsCodeFragment
import com.arsvechkarev.remailir.CoreActivity
import com.arsvechkarev.remailir.R
import com.arsvechkarev.remailir.entrance.di.DaggerEntranceComponent
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.Failed
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.OnCodeSent
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.remailir.entrance.presentation.PhoneAuthState.UserNotExist
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthProvider
import core.base.EntranceActivity
import core.di.coreComponent
import core.di.modules.ContextModule
import core.extensions.observe
import core.extensions.showToast
import core.extensions.switchFragment
import core.extensions.viewModelOf
import core.model.other.Country
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class EntranceActivity : AppCompatActivity(), EntranceActivity {
  
  private val smsCodeFragment = SmsCodeFragment()
  private val registrationFragment = RegistrationFragment()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  
  @Inject
  lateinit var phoneAuthProvider: PhoneAuthProvider
  
  private val viewModel by lazy { viewModelOf<EntranceViewModel>(viewModelFactory) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.decorView.systemUiVisibility =
      (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    setContentView(R.layout.activity_entrance)
    DaggerEntranceComponent.builder()
      .coreComponent(coreComponent)
      .contextModule(ContextModule(this))
      .build()
      .inject(this)
    viewModel.phoneState().observe(this, ::handleState)
    if (savedInstanceState == null) {
      switchFragment(R.id.rootContainer, PhoneFragment(), false)
    }
  }
  
  private fun handleState(state: PhoneAuthState) {
    when (state) {
      is OnCodeSent -> goToFragment(smsCodeFragment)
      is UserAlreadyExists -> {
        goToBase()
      }
      is UserNotExist -> goToFragment(registrationFragment)
      is Failed -> {
        if (state.exception is FirebaseAuthInvalidCredentialsException) {
          showToast("Code is invalid")
        }
      }
    }
  }
  
  override fun goToBase() {
    val intent = Intent(this, CoreActivity::class.java)
    startActivity(intent)
  }
  
  override fun onCountrySelected(country: Country) {
    repeat(supportFragmentManager.backStackEntryCount) {
      supportFragmentManager.popBackStack()
    }
    goToFragment(PhoneFragment.instance(country))
  }
  
  private fun goToFragment(fragment: Fragment) {
    switchFragment(R.id.rootContainer, fragment)
  }
  
  override fun onPhoneEntered(phoneNumber: String) {
    switchFragment(R.id.rootContainer, SmsCodeFragment(), true)
    phoneAuthProvider.verifyPhoneNumber(phoneNumber, 60, SECONDS, this, viewModel.callbacks)
  }
  
  override fun onCheckCode(code: String) {
    viewModel.checkCode(code)
  }
  
  override fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
    switchFragment(R.id.rootContainer, fragment, addToBackStack)
  }
  
}