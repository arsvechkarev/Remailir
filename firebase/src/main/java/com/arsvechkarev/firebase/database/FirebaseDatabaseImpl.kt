package com.arsvechkarev.firebase.database

import com.arsvechkarev.core.concurrency.Dispatchers
import com.arsvechkarev.core.extenstions.assertThat
import com.arsvechkarev.core.extenstions.await
import com.arsvechkarev.core.extenstions.waitForSingleValueEvent
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.withContext

class FirebaseDatabaseImpl(
  private val dispatchers: Dispatchers
) : Database {
  
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
      assertThat(value.isNotBlank())
      list.add(value)
    }
    return@withContext list
  }
  
  override suspend fun setValues(
    values: Map<String, Any>
  ): Unit = withContext(dispatchers.IO) {
    reference.updateChildren(values).await()
  }
}