package com.arsvechkarev.search.domain

import com.arsvechkarev.search.domain.RequestResult.ERROR_ALREADY_FRIENDS
import com.arsvechkarev.search.domain.RequestResult.ERROR_REQUEST_ALREADY_SENT
import com.arsvechkarev.search.domain.RequestResult.ERROR_THIS_USER_ALREADY_HAS_REQUEST
import com.arsvechkarev.search.domain.RequestResult.SENT
import com.arsvechkarev.testcommon.FakeDispatchers
import com.arsvechkarev.testcommon.FakeFirebaseDatabase
import com.arsvechkarev.testcommon.FakeJsonData.FullUsersDatabase
import com.arsvechkarev.testcommon.isExactlyTheSameAs
import com.arsvechkarev.testcommon.verify
import core.Dispatchers
import core.impl.firebase.PathDatabaseSchema
import core.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchRepositoryTest {
  
  private fun createSearchRepository(
    username: String,
    fakeDatabase: FakeFirebaseDatabase = FakeFirebaseDatabase(FullUsersDatabase),
    dispatchers: Dispatchers = FakeDispatchers
  ): SearchRepository {
    return SearchRepository(username, core.impl.firebase.PathDatabaseSchema, fakeDatabase,
      dispatchers)
  }
  
  @Test
  fun `Getting all users`() = runBlockingTest {
    val repository = createSearchRepository("a")
    val expectedList = listOf("b", "c", "d", "e", "lala").map { User(it) }
    
    val list: List<User> = repository.getUsersList(true)
    
    verify { expectedList isExactlyTheSameAs list }
  }
  
  @Test
  fun `Sending friend request`() = runBlockingTest {
    val repository = createSearchRepository("a")
    
    val result1 = repository.sendFriendRequest("c")
    val result2 = repository.sendFriendRequest("d")
    val result3 = repository.sendFriendRequest("b")
    val result4 = repository.sendFriendRequest("lala")
    
    verify { result1 == ERROR_ALREADY_FRIENDS }
    verify { result2 == ERROR_REQUEST_ALREADY_SENT }
    verify { result3 == ERROR_THIS_USER_ALREADY_HAS_REQUEST }
    verify { result4 == SENT }
  }
}