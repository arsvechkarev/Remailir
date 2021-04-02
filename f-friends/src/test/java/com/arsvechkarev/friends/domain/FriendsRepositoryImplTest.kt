package com.arsvechkarev.friends.domain

import com.arsvechkarev.core.UserMapper
import com.arsvechkarev.testcommon.FakeFirebaseDatabase
import com.arsvechkarev.testcommon.FakeJsonData.FullUsersDatabase
import com.arsvechkarev.testcommon.doesNotHave
import com.arsvechkarev.testcommon.has
import com.arsvechkarev.testcommon.shouldContainAll
import com.arsvechkarev.testcommon.user
import com.arsvechkarev.testcommon.usersList
import com.arsvechkarev.testcommon.verify
import core.impl.firebase.PathDatabaseSchema
import core.model.FriendsType.ALL_FRIENDS
import core.model.FriendsType.MY_REQUESTS
import core.model.FriendsType.REQUESTS_TO_ME
import core.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FriendsRepositoryImplTest {
  
  @Test
  fun `Fetching friends`() = runBlockingTest {
    val repository = createRepository("a")
    val users = repository.getListByType(ALL_FRIENDS)
    users shouldContainAll usersList("c", "e")
  }
  
  @Test
  fun `Fetching friend requests to this user`() = runBlockingTest {
    val repository = createRepository("d")
    val users = repository.getListByType(REQUESTS_TO_ME)
    users shouldContainAll usersList("e", "a")
  }
  
  @Test
  fun `Fetching friend requests from this user`() = runBlockingTest {
    val repository = createRepository("e")
    val users = repository.getListByType(MY_REQUESTS)
    users shouldContainAll usersList("d")
  }
  
  @Test
  fun `Removing friend`() = runBlockingTest {
    val testDatabase = FakeFirebaseDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryC = createRepository("c", testDatabase)
    
    val oldFriendsOfAList = repositoryA.getListByType(ALL_FRIENDS)
    val oldFriendsOfCList = repositoryC.getListByType(ALL_FRIENDS)
    //    val oldFriendsOfACachedList = repositoryA.getFromCache(ALL_FRIENDS)!!
    
    verify { oldFriendsOfAList has user("c") }
    //    verify { oldFriendsOfACachedList has user("c") }
    verify { oldFriendsOfCList has user("a") }
    
    repositoryA.removeFriend(user("c"))
    
    //    val newCachedFriendsOfAList = repositoryA.getFromCache(ALL_FRIENDS)!!
    val newFriendsOfAList = repositoryA.getListByType(ALL_FRIENDS)
    val newFriendsOfCList = repositoryC.getListByType(ALL_FRIENDS)
    
    //    verify { newCachedFriendsOfAList doesNotHave user("c") }
    verify { newFriendsOfAList doesNotHave user("c") }
    verify { newFriendsOfCList doesNotHave user("a") }
  }
  
  @Test
  fun `Canceling this user request`() = runBlockingTest {
    val testDatabase = FakeFirebaseDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryD = createRepository("d", testDatabase)
    val oldUserARequestsFromHim = repositoryA.getListByType(MY_REQUESTS)
    val oldUserDRequestsToHim = repositoryD.getListByType(REQUESTS_TO_ME)
    
    verify { oldUserARequestsFromHim has user("d") }
    verify { oldUserDRequestsToHim has user("a") }
    
    repositoryA.cancelMyRequest(user("d"))
    
    val newUserARequestsFromHim = repositoryA.getListByType(MY_REQUESTS)
    val newUserDRequestsToHim = repositoryD.getListByType(REQUESTS_TO_ME)
    
    verify { newUserARequestsFromHim doesNotHave user("d") }
    verify { newUserDRequestsToHim doesNotHave user("a") }
  }
  
  @Test
  fun `Dismissing request to this user`() = runBlockingTest {
    val testDatabase = FakeFirebaseDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryB = createRepository("b", testDatabase)
    val oldUserARequestsToHim = repositoryA.getListByType(REQUESTS_TO_ME)
    val oldUserBRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { oldUserARequestsToHim has user("b") }
    verify { oldUserBRequestsFromHim has user("a") }
    
    repositoryA.dismissRequest(user("b"))
    
    val newUserARequestsToHim = repositoryA.getListByType(REQUESTS_TO_ME)
    val newUserDRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { newUserARequestsToHim doesNotHave user("b") }
    verify { newUserDRequestsFromHim doesNotHave user("a") }
  }
  
  @Test
  fun `Accepting friend request`() = runBlockingTest {
    val testDatabase = FakeFirebaseDatabase(FullUsersDatabase)
    val repositoryA = createRepository("a", testDatabase)
    val repositoryB = createRepository("b", testDatabase)
    val oldUserAFriends = repositoryA.getListByType(ALL_FRIENDS)
    val oldUserBFriends = repositoryB.getListByType(ALL_FRIENDS)
    val oldUserARequestsToHim = repositoryA.getListByType(REQUESTS_TO_ME)
    val oldUserBRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { oldUserAFriends doesNotHave user("b") }
    verify { oldUserBFriends doesNotHave user("a") }
    verify { oldUserARequestsToHim has user("b") }
    verify { oldUserBRequestsFromHim has user("a") }
    
    repositoryA.acceptRequest(user("b"))
    
    val newUserAFriends = repositoryA.getListByType(ALL_FRIENDS)
    val newUserBFriends = repositoryB.getListByType(ALL_FRIENDS)
    val newUserARequestsToHim = repositoryA.getListByType(REQUESTS_TO_ME)
    val newUserDRequestsFromHim = repositoryB.getListByType(MY_REQUESTS)
    
    verify { newUserAFriends has user("b") }
    verify { newUserBFriends has user("a") }
    verify { newUserARequestsToHim doesNotHave user("b") }
    verify { newUserDRequestsFromHim doesNotHave user("a") }
    println(testDatabase.rootJsonObject)
  }
  
  private fun createRepository(
    username: String,
    database: FakeFirebaseDatabase = FakeFirebaseDatabase(FullUsersDatabase)
  ): FriendsRepositoryImpl {
    return FriendsRepositoryImpl(
      User(username),
      core.impl.firebase.PathDatabaseSchema,
      database,
      ByUsernameUsersActions,
      UserMapper
    )
  }
}