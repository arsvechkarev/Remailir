package com.arsvechkarev.remailir.main.registration

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechkarev.auth.presentation.fragments.PhoneFragment
import com.arsvechkarev.auth.presentation.fragments.RegistrationFragment
import com.arsvechkarev.auth.presentation.fragments.SmsCodeFragment
import com.arsvechkarev.core.declaration.EntranceActivity
import com.arsvechkarev.core.extensions.observe
import com.arsvechkarev.core.extensions.switchFragment
import com.arsvechkarev.core.extensions.viewModelOf
import com.arsvechkarev.core.model.Country
import com.arsvechkarev.remailir.R
import com.arsvechkarev.remailir.main.CoreActivity
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.OnCheckedAutomatically
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.OnCodeSent
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.UserAlreadyExists
import com.arsvechkarev.remailir.main.registration.PhoneAuthState.UserNotExist
import com.google.firebase.auth.PhoneAuthProvider
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
      is OnCodeSent -> goToFragment(smsCodeFragment, true)
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
  
  override fun goToFragment(fragment: Fragment, addToBackStack: Boolean) {
    switchFragment(R.id.rootContainer, fragment, addToBackStack)
  }
  
  override fun onPhoneEntered(phoneNumber: String) {
    phoneAuthProvider.verifyPhoneNumber(phoneNumber, 60, SECONDS, this, viewModel.callbacks)
  }
  
  override fun onCheckCode(code: String) {
    viewModel.checkCode(code)
  }
  
}
