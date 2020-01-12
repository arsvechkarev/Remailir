package storage

import androidx.work.Data

fun workersMap(vararg pairs: Pair<String, Any>): Data {
  return Data.Builder().putAll(pairs.toMap()).build()
}