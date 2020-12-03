package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.model.FriendsType.ALL_FRIENDS
import com.arsvechkarev.core.model.FriendsType.FRIENDS_REQUESTS
import com.arsvechkarev.core.model.FriendsType.MY_REQUESTS
import com.arsvechkarev.firebase.database.PathDatabaseSchema
import com.arsvechkarev.testcommon.TestDatabase
import com.arsvechkarev.testcommon.TestJson.FullUsersDatabase
import com.arsvechkarev.testcommon.doesNotContain
import com.arsvechkarev.testcommon.isEquivalentTo
import com.arsvechkarev.testcommon.shouldContainAll
import com.arsvechkarev.testcommon.verify
import com.arsvechkarev.testcommon.verifyFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FriendsRepositoryTest {
  
  private fun createRepository(
    username: String,
    database: TestDatabase = TestDatabase(FullUsersDatabase)
  ): FriendsRepository {
    return FriendsRepository(username, PathDatabaseSchema, database)
  }
  
  @Test
  fun `Fetching friends`() = runBlockingTest {
    val repository = createRepository("a")
    val users = repository.getListByType(ALL_FRIENDS)
    users shouldContainAll listOf("c", "e")
  }
  
  @Test
  fun `Fetching friend requests to this user`() = runBlockingTest {
    val repository = createRepository("d")
    val users = repository.getListByType(FRIENDS_REQUESTS)
    users shouldContainAll listOf("e", "a")
  }
  
  @Test
  fun `Fetching friend requests from this user`() = runBlockingTest {
    val repository = createRepository("e")
    val users = repository.getListByType(MY_REQUESTS)
    users shouldContainAll listOf("d")
  }
  
  @Test
  fun `Checking cache`() = runBlockingTest {
    val repository = createRepository("a")
    
    val myRequests = repository.getListByType(MY_REQUESTS)
    val cachedRequests = repository.getFromCache(MY_REQUESTS)!!
    
    verify { myRequests isEquivalentTo cachedRequests }
    verify { repository.hasCacheFor(MY_REQUESTS) }
    verifyFalse { repository.hasCacheFor(ALL_FRIENDS) }
    verifyFalse { repository.hasCacheFor(FRIENDS_REQUESTS) }
  }
  
  @Test
  fun `Removing friend`() = runBlockingTest {
    val testDatabase = TestDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryC = createRepository("c", testDatabase)
    val oldFriendsOfAList = repositoryA.getListByType(ALL_FRIENDS)
    val oldFriendsOfCList = repositoryC.getListByType(ALL_FRIENDS)
    val oldFriendsOfACachedList = repositoryA.getFromCache(ALL_FRIENDS)!!
    
    verify { oldFriendsOfAList.contains("c") }
    verify { oldFriendsOfACachedList.contains("c") }
    verify { oldFriendsOfCList.contains("a") }
    
    repositoryA.removeFriend("c")
    
    val newCachedFriendsOfAList = repositoryA.getFromCache(ALL_FRIENDS)!!
    val newFriendsOfAList = repositoryA.getListByType(ALL_FRIENDS)
    val newFriendsOfCList = repositoryC.getListByType(ALL_FRIENDS)
    
    verify { newCachedFriendsOfAList doesNotContain "c" }
    verify { newFriendsOfAList doesNotContain "c" }
    verify { newFriendsOfCList doesNotContain "a" }
  }
  
  @Test
  fun `Canceling this user request`() = runBlockingTest {
    val testDatabase = TestDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryD = createRepository("d", testDatabase)
    val oldUserARequestsFromHim = repositoryA.getListByType(MY_REQUESTS)
    val oldUserDRequestsToHim = repositoryD.getListByType(FRIENDS_REQUESTS)
    
    verify { oldUserARequestsFromHim.contains("d") }
    verify { oldUserDRequestsToHim.contains("a") }
    
    repositoryA.cancelMyRequest("d")
    
    val newUserARequestsFromHim = repositoryA.getListByType(MY_REQUESTS)
    val newUserDRequestsToHim = repositoryD.getListByType(FRIENDS_REQUESTS)
    
    verify { newUserARequestsFromHim doesNotContain "d" }
    verify { newUserDRequestsToHim doesNotContain "a" }
  }
  
  @Test
  fun `Dismissing request to this user`() = runBlockingTest {
    val testDatabase = TestDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryB = createRepository("b", testDatabase)
    val oldUserARequestsToHim = repositoryA.getListByType(FRIENDS_REQUESTS)
    val oldUserBRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { oldUserARequestsToHim.contains("b") }
    verify { oldUserBRequestsFromHim.contains("a") }
    
    repositoryA.dismissRequest("b")
    
    val newUserARequestsToHim = repositoryA.getListByType(FRIENDS_REQUESTS)
    val newUserDRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { newUserARequestsToHim doesNotContain "b" }
    verify { newUserDRequestsFromHim doesNotContain "a" }
  }
  
  @Test
  fun `Accepting friend request`() = runBlockingTest {
    val testDatabase = TestDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryB = createRepository("b", testDatabase)
    val oldUserAFriends = repositoryA.getListByType(ALL_FRIENDS)
    val oldUserBFriends = repositoryB.getListByType(ALL_FRIENDS)
    val oldUserARequestsToHim = repositoryA.getListByType(FRIENDS_REQUESTS)
    val oldUserBRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { oldUserAFriends doesNotContain "b" }
    verify { oldUserBFriends doesNotContain "a" }
    verify { oldUserARequestsToHim.contains("b") }
    verify { oldUserBRequestsFromHim.contains("a") }
    
    repositoryA.acceptRequest("b")
    
    val newUserAFriends = repositoryA.getListByType(ALL_FRIENDS)
    val newUserBFriends = repositoryB.getListByType(ALL_FRIENDS)
    val newUserARequestsToHim = repositoryA.getListByType(FRIENDS_REQUESTS)
    val newUserDRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { newUserAFriends.contains("b") }
    verify { newUserBFriends.contains("a") }
    verify { newUserARequestsToHim doesNotContain "b" }
    verify { newUserDRequestsFromHim doesNotContain "a" }
  }
}