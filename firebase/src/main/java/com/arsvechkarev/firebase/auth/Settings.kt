package com.arsvechkarev.firebase.auth

import com.google.firebase.auth.ActionCodeSettings

val AuthSettings: ActionCodeSettings
  get() = ActionCodeSettings.newBuilder()
      .setUrl("https://remailir.com/auth")
      .setHandleCodeInApp(true)
      .setAndroidPackageName("com.arsvechkarev.remailir", true, "1")
      .build()