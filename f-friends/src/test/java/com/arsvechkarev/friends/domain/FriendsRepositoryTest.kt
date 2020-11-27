package com.arsvechkarev.friends.domain

import com.arsvechkarev.testcommon.Json.FullUsersDatabase
import com.arsvechkarev.testcommon.TestDatabase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class FriendsRepositoryTest {
  
  private val testDatabase = TestDatabase(FullUsersDatabase)
  
  @Test
  fun f() {
    runBlockingTest {
      val usernamesBefore = testDatabase.getList("usernames")
      val result = ArrayList(usernamesBefore)
      result.add("mom")
      testDatabase.setValues(mapOf("usernames" to result))
      val usernamesAfter = testDatabase.getList("usernames")
      println("kkk1 = $usernamesBefore")
      println("kkk2 = $usernamesAfter")
    }
  }
}