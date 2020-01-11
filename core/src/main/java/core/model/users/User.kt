package core.model.users

import android.net.Uri
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
  
  companion object {
    fun empty() = User("", "", "", "")
  }
}

fun User.withNewImageUri(newUrl: Uri) = User(id, phone, name, newUrl.toString())

fun User.withNewProfileName(newName: String) = User(newName, phone, name, imageUrl)

fun List<User>.toOthers(): List<OtherUser> {
  val others = ArrayList<OtherUser>()
  this.forEach {
    others.add(OtherUser(it.id, it.name, it.imageUrl))
  }
  return others
}
