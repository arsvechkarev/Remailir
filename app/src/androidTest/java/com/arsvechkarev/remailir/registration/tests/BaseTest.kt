package com.arsvechkarev.remailir.registration.tests

import android.os.SystemClock.sleep
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.arsvechkarev.remailir.entrance.EntranceActivity
import com.arsvechkarev.remailir.registration.screens.PhoneScreen
import com.arsvechkarev.remailir.registration.screens.RegistrationScreen
import com.arsvechkarev.remailir.registration.screens.SmsCodeScreen
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BaseTest {
  
  @get:Rule
  val rule = ActivityTestRule(EntranceActivity::class.java)
  
  @Test
  fun test1_entering() {
    onScreen<PhoneScreen> {
      editTextPhone {
        // TODO (1/4/2020): Change hardcoded test number
        typeText("5555551234")
      }
      buttonNext.click()
    }
    
    onScreen<SmsCodeScreen> {
      editTextSmsCode.typeText("123456")
    }
  
    sleep(5000) // TODO (1/4/2020): Add idling resource
  
    onScreen<RegistrationScreen> {
      editTextUsername.isVisible()
    }
    
  }
}
