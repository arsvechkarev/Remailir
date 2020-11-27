package com.arsvechkarev.testcommon

object TestJson {
  
  const val FullUsersDatabase = """{
  "usernames": [
    "a",
    "b",
    "c",
    "d",
    "e",
    "lala"
  ],
  "users": {
    "a": {
      "email": "a@gmail.com",
      "friend_requests_from_me": [
        "d"
      ],
      "friend_requests_to_me": [
        "b"
      ],
      "friends": [
        "c",
        "e"
      ]
    },
    "b": {
      "email": "b@gmail.com",
      "friend_requests_from_me": [
        "a"
      ],
      "friend_requests_to_me": [],
      "friends": [
        "c",
        "e"
      ]
    },
    "c": {
      "email": "c@gmail.com",
      "friend_requests_from_me": [],
      "friend_requests_to_me": [],
      "friends": [
        "a",
        "b"
      ]
    },
    "d": {
      "email": "d@gmail.com",
      "friend_requests_from_me": [],
      "friend_requests_to_me": [
        "e",
        "a"
      ],
      "friends": []
    },
    "e": {
      "email": "e@gmail.com",
      "friend_requests_from_me": [
        "d"
      ],
      "friend_requests_to_me": [],
      "friends": [
        "a",
        "b"
      ]
    },
    "lala": {
      "email": "arsvechkarev@gmail.com",
      "friend_requests_from_me": [],
      "friend_requests_to_me": [],
      "friends": []
    }
  }
}"""

}