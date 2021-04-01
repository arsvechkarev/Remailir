package com.arsvechkarev.testcommon

/**
 * Fake mvp view for tests that helps deaing with [ScreenState]
 */
@Suppress("UNCHECKED_CAST")
abstract class FakeBaseScreen {
  
  @PublishedApi
  internal var _currentState: ScreenState = ScreenState.None
  
  @PublishedApi
  internal val recordedStates = ArrayList<ScreenState>()
  
  protected fun updateState(state: ScreenState) {
    recordedStates.add(state)
    _currentState = state
  }
  
  fun currentState(): ScreenState = _currentState
  
  inline fun <reified T> currentSuccessState(): ScreenState.Success<T> {
    return _currentState as ScreenState.Success<T>
  }
  
  ///////////////////////////
  //    Testing helpers    //
  ///////////////////////////
  
  fun hasStatesCount(size: Int): Boolean {
    return recordedStates.size == size
  }
  
  inline fun <reified T : ScreenState> stateAtPosition(position: Int): T {
    return recordedStates[position] as T
  }
  
  inline fun <reified T> stateAtPositionIs(position: Int): Boolean {
    return recordedStates[position] is T
  }
  
  inline fun <reified T> currentStateIs(): Boolean {
    return _currentState is T
  }
  
  inline fun <reified T> currentSuccessStateHasData(data: T): Boolean {
    return (_currentState as ScreenState.Success<T>).data == data
  }
}