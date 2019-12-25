package core.base

import androidx.fragment.app.Fragment
import core.model.other.Country

interface EntranceActivity {
  
  fun goToBase()
  
  fun onCountrySelected(country: Country)
  
  fun onPhoneEntered(phoneNumber: String)
  
  fun onCheckCode(code: String)
  
  fun goToFragment(fragment: Fragment, addToBackStack: Boolean)
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity