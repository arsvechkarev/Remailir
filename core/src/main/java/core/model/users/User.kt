package core.model.users

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class User(
  val id: String,
  val username: String,
  val email: String,
  val password: String,
  val imageUrl: String
) : Parcelable, Serializable {
  constructor() : this("", "", "", "", "")
}
@Parcelize
data class NewUser(
  val id: String,
  val phone: String,
  val name: String,
  val imageUrl: String
) : Parcelable, Serializable {
  constructor() : this("", "", "", "")
}