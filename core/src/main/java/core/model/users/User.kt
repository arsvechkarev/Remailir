package core.model.users

import android.os.Parcelable
import core.recycler.DisplayableItem
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class User(
  override val id: String,
  val phone: String,
  val name: String,
  val imageUrl: String
) : Parcelable, Serializable, DisplayableItem {
  constructor() : this("", "", "", "")
  
  companion object {
    fun empty() = User("", "", "", "")
  }
}
