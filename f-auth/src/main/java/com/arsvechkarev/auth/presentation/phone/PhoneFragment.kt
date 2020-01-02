package com.arsvechkarev.auth.presentation.phone

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.utils.phoneNumber
import com.arsvechkarev.auth.utils.removeDashes
import com.google.i18n.phonenumbers.PhoneNumberUtil
import core.base.BaseFragment
import core.base.entranceActivity
import core.extensions.hideKeyboard
import core.model.other.Country
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.layoutCountryCode
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode
import java.util.Locale


class PhoneFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_phone
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    addTextWatcher(Locale.getDefault().country)
    textCountryCode.text =
      "+${PhoneNumberUtil.getInstance().getCountryCodeForRegion(Locale.getDefault().country)}"
    buttonNext.setOnClickListener {
      val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      entranceActivity.onPhoneEntered(phoneNumber)
    }
    layoutCountryCode.setOnClickListener {
      hideKeyboard(editTextPhone)
      entranceActivity.goToFragment(CountriesFragment(), true)
    }
  }
  
  private fun addTextWatcher(countryLetters: String) {
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher(countryLetters) {
      override fun afterTextChanged(s: Editable) {
        super.afterTextChanged(s)
        buttonNext.isEnabled = s.removeDashes().length >= 10
      }
    })
  }
  
  fun onCountrySelected(country: Country) {
    addTextWatcher(country.letters)
    textCountryCode.text = "+${country.code}"
  }
}
