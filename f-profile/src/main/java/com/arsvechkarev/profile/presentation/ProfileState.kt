package com.arsvechkarev.profile.presentation

import core.model.users.User

sealed class ProfileState {
  class Success(val user: User) : ProfileState()
  class Failure(val exception: Throwable) : ProfileState()
}