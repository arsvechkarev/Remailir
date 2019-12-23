package core.declaration

import androidx.fragment.app.Fragment
import core.model.Country

interface EntranceActivity {
  
  fun goToBase()
  
  fun onCountrySelected(country: Country)
  
  fun goToFragment(
    fragment: Fragment,
    addToBackStack: Boolean = false
  )
  
  fun onPhoneEntered(phoneNumber: String)
  
  fun onCheckCode(code: String)
}

val Fragment.entranceActivity: EntranceActivity
  get() = activity as EntranceActivity