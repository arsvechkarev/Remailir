package com.arsvechkarev.core.model.users

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtherUser(
  val id: String,
  val username: String,
  val imageUrl: String
) : Parcelable {
  constructor() : this("", "", "")
}