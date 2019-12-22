package com.arsvechkarev.auth.utils

import android.text.Editable
import android.widget.EditText
import com.arsvechkarev.core.model.Country
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.util.Locale

fun Editable?.removeDashes(): String {
  var p = 0
  if (this == null) return ""
  while (p < this.length) {
    if (this[p] == '-') {
      this.delete(p, p + 1)
    } else {
      p++
    }
  }
  return this.toString()
}

fun EditText?.phoneNumber(): String {
  return this?.text.toString().normalize()
}

fun String.normalize(): String {
  return this.replace(Regex("[ ()\\-]"), "")
}

fun getCountries(): List<Country> {
  val phoneNumberUtil = PhoneNumberUtil.getInstance()
  val countries = ArrayList<Country>()
  Locale.getISOCountries().forEach {
    val locale = Locale("", it)
    val code = phoneNumberUtil.getCountryCodeForRegion(it)
    if (code != 0) {
      val country = Country(locale.displayName, it, code)
      println(countries)
      countries.add(country)
    }
  }
  countries.sortBy { it.name  }
  return countries
}
