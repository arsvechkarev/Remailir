package com.arsvechkarev.auth.presentation.fragments

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import com.arsvechakrev.auth.R
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
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher() {
      override fun afterTextChanged(s: Editable?) {
        super.afterTextChanged(s)
        buttonNext.isEnabled = s.removeDashes().length >= 10
      }
    })
    buttonNext.setOnClickListener {
      val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      entranceActivity.onPhoneEntered(phoneNumber)
    }
    layoutCountryCode.setOnClickListener {
      activity!!.supportFragmentManager.beginTransaction()
        .add(R.id.rootLayoutPhoneFragment, CountryCodeFragment())
        .addToBackStack(null)
        .commit()
    }
  }
  
  fun onCountrySelected(country: Country) {
    textCountryCode.text = "+${country.code}"
  }
  
}
