package com.arsvechkarev.core.model.users

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ThisUser(
  val id: String,
  val username: String,
  val email: String,
  val password: String,
  val imageUrl: String
) : Parcelable {
  constructor() : this("", "", "", "", "")
}