package com.arsvechkarev.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
  val id: String,
  val username: String,
  val email: String,
  val password: String,
  val imageUrl: String
) : Parcelable {
  constructor() : this("", "", "", "", "")
}