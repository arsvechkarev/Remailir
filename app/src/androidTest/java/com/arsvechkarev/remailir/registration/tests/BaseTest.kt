package com.arsvechkarev.remailir.registration.tests

import android.os.SystemClock
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.screen.Screen.Companion.onScreen
import com.arsvechkarev.remailir.entrance.EntranceActivity
import com.arsvechkarev.remailir.registration.screens.PhoneScreen
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
    SystemClock.sleep(1000)
    
    onScreen<PhoneScreen> {
      editTextPhone {
        typeText("6666661234")
      }
      SystemClock.sleep(3000)
      buttonNext.click()
    }
    
    SystemClock.sleep(3000)
    
    onScreen<SmsCodeScreen> {
      editTextSmsCode.typeText("123456")
    }
    
    SystemClock.sleep(5000) // TODO (1/4/2020): Add idling resource
    
  }
}
