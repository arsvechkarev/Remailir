package com.arsvechkarev.core.model

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