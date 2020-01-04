package com.arsvechkarev.remailir.registration.tests

import com.arsvechkarev.remailir.utils.FirebaseFirestoreTest
import org.junit.ClassRule
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(BaseTest::class)
object RegistrationSuites {
  
  @get:ClassRule
  @JvmStatic
  val resource = FirebaseFirestoreTest.externalResource
  
}