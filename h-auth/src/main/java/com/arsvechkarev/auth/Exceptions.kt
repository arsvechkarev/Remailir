package com.arsvechkarev.auth

class WeakPasswordException : Exception {
  
  constructor() : super()
  constructor(message: String) : super(message)
}

class ShortNicknameException : Exception {
  
  constructor() : super()
  constructor(message: String) : super(message)
}

class UsernameAlreadyExistsException : Exception {
  
  constructor() : super()
  constructor(message: String) : super(message)
}
