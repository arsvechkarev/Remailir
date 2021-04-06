package core.ui.statemachine

interface Action

interface State

interface SideEffectsListener<A> {
  
  fun onActionCalled(action: A)
}

interface Reducer<A : Action, S : State> {
  
  fun reduce(action: A, sideEffectsListener: SideEffectsListener<A>): S
}