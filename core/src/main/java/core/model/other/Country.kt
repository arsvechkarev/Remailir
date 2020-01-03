package core.model.other

import android.os.Parcelable
import core.recycler.DisplayableItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
  val name: String,
  val letters: String,
  val code: Int
) : DisplayableItem, Parcelable {
  @IgnoredOnParcel
  override val id = name
  @IgnoredOnParcel
  override val type = TYPE_COUNTRY
}
