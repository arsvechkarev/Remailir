package com.arsvechkarev.auth.presentation.phone

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.arsvechakrev.auth.R
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
    val country = arguments?.getParcelable(COUNTRY) as? Country
    editTextPhone.doAfterTextChanged {
      buttonNext.isEnabled = it.removeDashes().length >= 10
    }
    if (country != null) {
      textCountryCode.text =
        getString(R.string.format_country_phone_prefix, country.code.toString())
    } else {
      val region =
        PhoneNumberUtil.getInstance().getCountryCodeForRegion(Locale.getDefault().country)
      textCountryCode.text = getString(R.string.format_country_phone_prefix, region.toString())
    }
    buttonNext.setOnClickListener {
      val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      entranceActivity.onPhoneEntered(phoneNumber)
    }
    layoutCountryCode.setOnClickListener {
      hideKeyboard(editTextPhone)
      entranceActivity.goToCountriesList()
    }
  }
  
  companion object {
    
    private const val COUNTRY = "COUNTRY"
  
    fun create(country: Country) = PhoneFragment().apply {
      Bundle().apply {
        putParcelable(COUNTRY, country)
      }
    }
  }
}
