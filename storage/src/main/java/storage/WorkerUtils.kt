package storage

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest

/**
 * Creates worker with required network connection and optional input data
 */
inline fun <reified T : ListenableWorker> networkWorkerOf(inputData: Data? = null): OneTimeWorkRequest {
  val networkConstraints = Constraints.Builder()
    .setRequiredNetworkType(NetworkType.CONNECTED)
    .build()
  
  val builder = OneTimeWorkRequest.Builder(T::class.java).setConstraints(networkConstraints)
  if (inputData != null) {
    builder.setInputData(inputData)
  }
  return builder.build()
}

fun workersMap(vararg pairs: Pair<String, Any>): Data {
  return Data.Builder().putAll(pairs.toMap()).build()
}