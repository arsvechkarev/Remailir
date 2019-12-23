package com.arsvechkarev.remailir.entrance

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.presentation.fragments.PhoneFragment
import com.arsvechkarev.auth.presentation.fragments.RegistrationFragment
import com.arsvechkarev.auth.presentation.fragments.SmsCodeFragment
import com.arsvechkarev.remailir.CoreActivity
import com.arsvechkarev.remailir.R
import com.arsvechkarev.remailir.entrance.PhoneAuthState.OnCodeSent
import com.arsvechkarev.remailir.entrance.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.remailir.entrance.PhoneAuthState.UserNotExist
import com.google.firebase.auth.PhoneAuthProvider
import core.base.EntranceActivity
import core.extensions.observe
import core.extensions.switchFragment
import core.extensions.viewModelOf
import core.model.other.Country
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Inject

class EntranceActivity : AppCompatActivity(), EntranceActivity {
  
  private val phoneFragment = PhoneFragment()
  private val smsCodeFragment = SmsCodeFragment()
  private val registrationFragment = RegistrationFragment()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  
  @Inject
  lateinit var phoneAuthProvider: PhoneAuthProvider
  
  private val viewModel by lazy { viewModelOf<EntranceViewModel>(viewModelFactory) }
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_entrance)
    DaggerEntranceComponent.create().inject(this)
    viewModel.phoneState().observe(this, ::handleState)
    switchFragment(R.id.rootContainer, phoneFragment, false)
  }
  
  private fun handleState(state: PhoneAuthState) {
    when (state) {
      is OnCodeSent -> goToFragment(smsCodeFragment)
      is UserAlreadyExists -> goToBase()
      is UserNotExist -> goToFragment(registrationFragment)
    }
  }
  
  override fun goToBase() {
    val intent = Intent(this, CoreActivity::class.java)
    startActivity(intent)
  }
  
  override fun onCountrySelected(country: Country) {
    phoneFragment.onCountrySelected(country)
  }
  
  private fun goToFragment(fragment: Fragment) {
    switchFragment(R.id.rootContainer, fragment)
  }
  
  override fun onPhoneEntered(phoneNumber: String) {
    phoneAuthProvider.verifyPhoneNumber(phoneNumber, 60, SECONDS, this, viewModel.callbacks)
  }
  
  override fun onCheckCode(code: String) {
    viewModel.checkCode(code)
  }
  
}
