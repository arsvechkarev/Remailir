package core.model.other

import android.os.Parcelable
import core.recycler.DisplayableItem
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Represents a country
 *
 * @param name Country name, e.g "Australia"
 * @param letters two letters that shows a country, e.g "UK"
 * @param code Country phone number code, e.g "+31"
 */
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
