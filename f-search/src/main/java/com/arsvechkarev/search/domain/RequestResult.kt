package com.arsvechkarev.search.domain

enum class RequestResult {
  SENT,
  ERROR_ALREADY_FRIENDS,
  ERROR_REQUEST_ALREADY_SENT,
  ERROR_THIS_USER_ALREADY_HAS_REQUEST,
}