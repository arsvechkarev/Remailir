package firebase.chat

interface FirebaseChatRequestsDataSourceProvider {
  
  fun provideChatRequestsDataSource(): ChatsRequestsDataSource
  
  companion object {
    
    private var _instance: FirebaseChatRequestsDataSourceProvider? = null
    val instance: FirebaseChatRequestsDataSourceProvider
      get() = _instance ?: error("You should call initialize() first")
    
    fun initialize(firebaseChatRequestsDataSourceProvider: FirebaseChatRequestsDataSourceProvider) {
      _instance = firebaseChatRequestsDataSourceProvider
    }
  }
}