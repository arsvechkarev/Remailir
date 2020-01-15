package com.arsvechkarev.auth.presentation.phone

import android.os.Bundle
import android.os.Handler
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.view.View
import androidx.core.os.postDelayed
import com.arsvechakrev.auth.R
import com.arsvechkarev.auth.presentation.countrycodes.CountriesFragment
import com.arsvechkarev.auth.utils.removeDashes
import com.arsvechkarev.views.LoadingDialog
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
  private val loadingDialog = LoadingDialog()
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    val country = arguments?.getParcelable(COUNTRY) as? Country
    if (country != null) {
      setTextWatcher(country.letters)
      textCountryCode.text = "+${country.code}"
    } else {
      setTextWatcher(Locale.getDefault().country)
      textCountryCode.text =
        "+${PhoneNumberUtil.getInstance().getCountryCodeForRegion(Locale.getDefault().country)}"
    }
    buttonNext.setOnClickListener {
      //            val phoneNumber = textCountryCode.text.toString() + editTextPhone.phoneNumber()
      //            entranceActivity.onPhoneEntered(phoneNumber)
      loadingDialog.show(childFragmentManager, null)
      Handler().postDelayed(6000) {
        loadingDialog.dismiss()
      }
    }
    layoutCountryCode.setOnClickListener {
      hideKeyboard(editTextPhone)
      entranceActivity.goToFragment(CountriesFragment(), true)
    }
  }
  
  private fun setTextWatcher(countryLetters: String) {
    editTextPhone.addTextChangedListener(object : PhoneNumberFormattingTextWatcher(countryLetters) {
      override fun afterTextChanged(s: Editable) {
        super.afterTextChanged(s)
        buttonNext.isEnabled = s.removeDashes().length >= 10
      }
    })
  }
  
  fun onCountrySelected(country: Country) {
    setTextWatcher(country.letters)
    textCountryCode.text = "+${country.code}"
  }
  
  companion object {
    
    private const val COUNTRY = "COUNTRY"
    
    fun instance(country: Country): PhoneFragment {
      val bundle = Bundle()
      bundle.putParcelable(COUNTRY, country)
      val fragment = PhoneFragment()
      fragment.arguments = bundle
      return fragment
    }
  }
}
