package com.arsvechkarev.auth.utils

import android.text.Editable
import android.widget.EditText
import com.google.i18n.phonenumbers.PhoneNumberUtil
import core.model.other.Country
import core.model.other.Letter
import core.recycler.DisplayableItem
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

fun getCountriesList(): MutableList<Country> {
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
  countries.sortBy { it.name }
  return countries
}

fun getCountriesWithLetters(countries: List<Country>): MutableList<DisplayableItem> {
  val resultList = ArrayList<DisplayableItem>()
  for (i in countries.indices) {
    if (i == 0) {
      resultList += Letter(countries[0].name.first().toString())
    }
    resultList += countries[i]
    if (i < countries.size - 1) {
      if (countries[i].name.first() != countries[i + 1].name.first()) {
        resultList += Letter(countries[i + 1].name.first().toString())
      }
    }
  }
  return resultList
}

