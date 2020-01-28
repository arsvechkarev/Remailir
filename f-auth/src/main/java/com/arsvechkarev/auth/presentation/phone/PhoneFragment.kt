package com.arsvechkarev.auth.presentation.phone

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.common.EntranceViewModel
import com.arsvechkarev.auth.common.PhoneAuthState
import com.arsvechkarev.auth.common.PhoneAuthState.Failed
import com.arsvechkarev.auth.common.PhoneAuthState.OnCodeSent
import com.arsvechkarev.auth.common.PhoneAuthState.Pending
import com.arsvechkarev.auth.di.DaggerAuthComponent
import com.arsvechkarev.auth.utils.phoneNumber
import com.arsvechkarev.auth.utils.removeDashes
import com.arsvechkarev.views.dialogs.LoadingDialog
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import core.base.entranceActivity
import core.di.coreComponent
import core.extensions.dismissSafely
import core.extensions.hideKeyboard
import core.extensions.observe
import core.extensions.show
import core.extensions.viewModelOf
import core.model.other.Country
import core.strings.ERROR_INVALID_PHONE_NUMBER
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.layoutCountryCode
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode
import kotlinx.android.synthetic.main.fragment_sms_code.textError
import java.util.Locale
import javax.inject.Inject


class PhoneFragment : Fragment(R.layout.fragment_phone) {
  
  private val loadingDialog = LoadingDialog()
  
  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory
  private lateinit var entranceViewModel: EntranceViewModel
  
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    DaggerAuthComponent.builder()
      .coreComponent(coreComponent)
      .build()
      .inject(this)
    entranceViewModel = requireActivity().viewModelOf(viewModelFactory) {
      observe(phoneState(), ::handleState)
    }
    
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val country = arguments?.getParcelable(COUNTRY) as? Country
    if (country != null) {
      textCountryCode.text =
        getString(R.string.format_country_phone_prefix, country.code.toString())
    } else {
      val region =
        PhoneNumberUtil.getInstance().getCountryCodeForRegion(Locale.getDefault().country)
      textCountryCode.text = getString(R.string.format_country_phone_prefix, region.toString())
    }
    textError.text = ""
    editTextPhone.doAfterTextChanged {
      textError.text = ""
      buttonNext.isEnabled = it.removeDashes().length >= 10
    }
    buttonNext.setOnClickListener {
      val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      entranceActivity.onPhoneEntered(phoneNumber)
      show(loadingDialog)
    }
    layoutCountryCode.setOnClickListener {
      hideKeyboard(editTextPhone)
      entranceActivity.goToCountriesList()
    }
  }
  
  
  private fun handleState(state: PhoneAuthState) {
    when (state) {
      is Pending -> {
        textError.text = ""
      }
      is OnCodeSent -> {
        loadingDialog.dismissSafely()
      }
      is Failed -> {
        loadingDialog.dismissSafely()
        if (state.exception is FirebaseAuthInvalidCredentialsException
          && state.exception.errorCode == ERROR_INVALID_PHONE_NUMBER
        ) {
          textError.text = getString(R.string.error_invalid_phone_number)
        }
      }
    }
  }
  
  companion object {
    
    private const val COUNTRY = "COUNTRY"
  
    fun create(country: Country) = PhoneFragment().apply {
      arguments = Bundle().apply {
        putParcelable(COUNTRY, country)
      }
    }
  }
}
