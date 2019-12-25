package com.arsvechkarev.auth.presentation.phone

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.countrycodes.CountryCodeFragment
import com.arsvechkarev.auth.utils.phoneNumber
import com.arsvechkarev.auth.utils.removeDashes
import core.base.BaseFragment
import core.base.entranceActivity
import core.model.other.Country
import kotlinx.android.synthetic.main.fragment_phone.buttonNext
import kotlinx.android.synthetic.main.fragment_phone.editTextPhone
import kotlinx.android.synthetic.main.fragment_phone.layoutCountryCode
import kotlinx.android.synthetic.main.fragment_phone.textCountryCode

class PhoneFragment : BaseFragment() {
  
  override val layout: Int = R.layout.fragment_phone
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    addTextWatcher()
    buttonNext.setOnClickListener {
      val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      entranceActivity.onPhoneEntered(phoneNumber)
    }
    layoutCountryCode.setOnClickListener {
      activity!!.supportFragmentManager.beginTransaction()
        .add(
          R.id.rootLayoutPhoneFragment,
          CountryCodeFragment()
        )
        .addToBackStack(null)
        .commit()
    }
  }
  
  private fun addTextWatcher() {
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
      override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        buttonNext.isEnabled = s.removeDashes().length >= 10
      }
    })
  }
  
  fun onCountrySelected(country: Country) {
    addTextWatcher()
    textCountryCode.text = "+${country.code}"
  }
  
}
