package firebase.impl

import com.google.firebase.database.FirebaseDatabase
import core.Dispatchers
import core.utils.await
import core.utils.waitForSingleValueEvent
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseDatabaseImpl @Inject constructor(
  private val dispatchers: Dispatchers
) : firebase.database.FirebaseDatabase {
  
  private val reference = FirebaseDatabase.getInstance().reference
  
  override suspend fun getList(
    path: String
  ): MutableList<String> = withContext(dispatchers.IO) {
    val snapshot = reference.child(path).waitForSingleValueEvent()
    if (!snapshot.exists() || !snapshot.hasChildren()) {
      return@withContext ArrayList()
    }
    val list = ArrayList<String>()
    for (snap in snapshot.children) {
      val value = snap.value as String
      require(value.isNotBlank())
      list.add(value)
    }
    return@withContext list
  }
  
  override suspend fun setValues(
    map: Map<String, Any>
  ): Unit = withContext(dispatchers.IO) {
    reference.updateChildren(map).await()
  }
}