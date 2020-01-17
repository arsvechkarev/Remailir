package core.base

import androidx.fragment.app.Fragment
import core.model.other.Country

interface EntranceActivity {
  
  fun goToCore()
  
  fun onCountrySelected(country: Country)
  
  fun onPhoneEntered(phoneNumber: String)
  
  fun onCheckCode(code: String)
  
  fun goToCountriesList()
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity