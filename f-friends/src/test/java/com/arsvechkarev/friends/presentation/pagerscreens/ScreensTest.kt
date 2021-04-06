package com.arsvechkarev.friends.presentation.pagerscreens

import com.arsvechkarev.friends.domain.FriendsInteractor
import com.arsvechkarev.friends.domain.FriendsRepositoryImpl
import com.arsvechkarev.friends.domain.FriendsScreensCommunicator
import com.arsvechkarev.friends.presentation.pagerscreens.allfriends.AllFriendsPresenter
import com.arsvechkarev.friends.presentation.pagerscreens.fakescreens.FakeAllFriendsScreen
import com.arsvechkarev.friends.presentation.pagerscreens.fakescreens.FakeRequestsToMeScreen
import com.arsvechkarev.friends.presentation.pagerscreens.myrequests.MyRequestsPresenter
import com.arsvechkarev.friends.presentation.pagerscreens.requeststome.RequestsToMePresenter
import com.arsvechkarev.testcommon.FakeDispatchers
import com.arsvechkarev.testcommon.FakeFirebaseDatabase
import com.arsvechkarev.testcommon.FakeJsonData.FullUsersDatabase
import com.arsvechkarev.testcommon.ScreenState.Loading
import com.arsvechkarev.testcommon.ScreenState.Success
import com.arsvechkarev.testcommon.user
import com.arsvechkarev.testcommon.verify
import core.StringToUserMapper
import core.model.User
import firebase.database.ByUsernameUsersActions
import firebase.impl.PathDatabaseSchema
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("TestFunctionName")
class ScreensTest {
  
  // TODO (4/2/2021): Write unit test for pager screens
  
  private var fakeDatabase: FakeFirebaseDatabase? = null
  
  @Before
  fun setUp() {
    fakeDatabase = FakeFirebaseDatabase(FullUsersDatabase)
  }
  
  @After
  fun tearDown() {
    fakeDatabase = null
  }
  
  @Test
  fun `All friends screen showing friends list`() = runBlockingTest {
    val user = user("a")
    val interactor = FriendsInteractor(FriendsRepository(user), FakeDispatchers)
    val allFriendsPresenter = AllFriendsPresenter(interactor, FriendsScreensCommunicator())
    val fakeScreen = FakeAllFriendsScreen()
    val friendsPath = PathDatabaseSchema.friendsPath(user)
    val mapper = StringToUserMapper()
    val friendsFromDatabase = fakeDatabase!!.getList(friendsPath).map(mapper::map)
    
    allFriendsPresenter.attachView(fakeScreen)
    
    with(fakeScreen) {
      verify { hasStatesCount(2) }
      verify { stateAtPositionIs<Loading>(0) }
      verify { currentStateIs<Success<List<User>>>() }
      assertEquals(friendsFromDatabase, currentSuccessState<List<User>>().data)
    }
  }
  
  @Test
  fun `Accepting request`() = runBlockingTest {
    val thisUser = user("a")
    val interactor = FriendsInteractor(FriendsRepository(thisUser), FakeDispatchers)
    val screensBridge = FriendsScreensCommunicator()
    val allFriendsScreen = FakeAllFriendsScreen()
    val requestsToMeScreen = FakeRequestsToMeScreen()
    AllFriendsPresenter(interactor, screensBridge).apply {
      attachView(allFriendsScreen)
    }
    RequestsToMePresenter(interactor, screensBridge).apply {
      attachView(requestsToMeScreen)
      startLoadingRequestsToMe()
      startListeningToRequestsToMeChanges()
    }
    
    val userB = user("b")
    interactor.acceptRequest(userB)
    screensBridge.notifyRequestAccepted()
    
    with(allFriendsScreen) {
      hasStatesCount(3)
      stateAtPositionIs<Loading>(0)
      stateAtPositionIs<Success<List<User>>>(1)
      stateAtPositionIs<Success<List<User>>>(2)
      
      //      val mapper = StringToUserMapper()
      //      val friendsPath = PathDatabaseSchema.friendsPath(thisUser)
      //      val friendsOfThisUser = fakeDatabase!!.getList(friendsPath).map(mapper::map)
      //      assertEquals(friendsOfThisUser, currentSuccessState<List<User>>().data)
    }
    with(requestsToMeScreen) {
      hasStatesCount(3)
      stateAtPositionIs<Loading>(0)
      stateAtPosition<Success<List<User>>>(1).apply {
        assertEquals(1, data.size) // Should have no requests anymore
      }
    }
  }
  
  private fun AllFriendsPresenter(
    interactor: FriendsInteractor,
    friendsScreensCommunicator: FriendsScreensCommunicator
  ): AllFriendsPresenter {
    return AllFriendsPresenter(interactor, friendsScreensCommunicator, FakeDispatchers)
  }
  
  private fun MyRequestsPresenter(
    interactor: FriendsInteractor,
    friendsScreensCommunicator: FriendsScreensCommunicator
  ): MyRequestsPresenter {
    return MyRequestsPresenter(interactor, friendsScreensCommunicator, FakeDispatchers)
  }
  
  private fun RequestsToMePresenter(
    interactor: FriendsInteractor,
    friendsScreensCommunicator: FriendsScreensCommunicator
  ): RequestsToMePresenter {
    return RequestsToMePresenter(interactor, friendsScreensCommunicator, FakeDispatchers)
  }
  
  private fun FriendsRepository(user: User): FriendsRepositoryImpl {
    return FriendsRepositoryImpl(user, PathDatabaseSchema, fakeDatabase!!,
      ByUsernameUsersActions, StringToUserMapper())
  }
}