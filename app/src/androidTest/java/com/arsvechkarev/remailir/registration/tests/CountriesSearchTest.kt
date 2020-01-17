package com.arsvechkarev.remailir.registration.tests

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.arsvechkarev.remailir.entrance.presentation.ActualEntranceActivity
import com.arsvechkarev.remailir.registration.screens.CountriesListScreen
import com.arsvechkarev.remailir.registration.screens.PhoneScreen
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class CountriesSearchTest {
  
  @get:Rule
  val rule = ActivityTestRule(ActualEntranceActivity::class.java)
  
  @Test
  fun test1_Countries_search() {
    
    onScreen<PhoneScreen> {
      layoutCountryCode.click()
    }
    
    onScreen<CountriesListScreen> {
    
    }
  }
}