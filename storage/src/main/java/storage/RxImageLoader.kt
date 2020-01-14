package storage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import java.io.InputStream
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object RxImageLoader {
  
  fun downloadImage(url: String): Single<Bitmap> {
    return Single.create { emitter ->
      try {
        val connection: HttpsURLConnection = URL(url).openConnection() as HttpsURLConnection
        connection.connect()
        val input: InputStream = connection.inputStream
        val bitmap = BitmapFactory.decodeStream(input)
        emitter.onSuccess(bitmap)
      } catch (e: Throwable) {
        emitter.onError(e)
      }
    }
  }
}