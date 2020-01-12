package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import core.strings.DEFAULT_IMG_URL
import io.reactivex.Maybe
import log.Loggable
import log.log
import java.io.InputStream
import java.net.URL
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection


class ProfileNetworkRequests @Inject constructor() : Loggable {
  
  override val logTag = "ProfileStuff"
  
  fun loadProfileImage(imageUrl: String): Maybe<Bitmap> {
    return Maybe.create { emitter ->
      if (imageUrl == DEFAULT_IMG_URL) {
        log { "url is default, return completion" }
        emitter.onComplete()
      } else {
        try {
          log { "loading from url" }
          val url = URL(imageUrl)
          val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
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
  
}