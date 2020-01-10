package core.model.users

import android.os.Parcelable
import core.recycler.DisplayableItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OtherUser(
  override val id: String,
  val username: String,
  val imageUrl: String
) : Parcelable, DisplayableItem {
  constructor() : this("", "", "")
}