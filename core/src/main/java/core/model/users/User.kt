package core.model.users

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class User(
  val id: String,
  val phone: String,
  val name: String,
  val imageUrl: String
) : Parcelable, Serializable {
  constructor() : this("", "", "", "")
}

fun List<User>.toOthers(): List<OtherUser> {
  val others = ArrayList<OtherUser>()
  this.forEach {
    others.add(OtherUser(it.id, it.name, it.imageUrl))
  }
  return others
}
