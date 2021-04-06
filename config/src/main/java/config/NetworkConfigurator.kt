package config

/**
 * Represents configuration values related to network
 */
object NetworkConfigurator {
  
  private var _fakeNetworkDelay = 800L
  private var _requestTimeout = 15000L
  private var _retryCount = 3L
  
  /** Fake network delay to so that loading wouldn't flash */
  val fakeNetworkDelay get() = _fakeNetworkDelay
  
  /** Max request timeout */
  val requestTimeout get() = _requestTimeout
  
  /** Max retry count for request */
  val retryCount get() = _retryCount
  
  fun configureFakeNetworkDelay(fakeNetworkDelay: Long) {
    _fakeNetworkDelay = fakeNetworkDelay
  }
  
  fun configureRequestTimeout(requestTimeout: Long) {
    _requestTimeout = requestTimeout
  }
  
  fun configureRetryCount(retryCount: Long) {
    _retryCount = retryCount
  }
}