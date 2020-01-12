package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import core.strings.DEFAULT_IMG_URL
import io.reactivex.Maybe
import log.Loggable
import log.log
import storage.RxImageLoader
import javax.inject.Inject


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
          RxImageLoader.downloadImage(imageUrl)
            .subscribe { bitmap -> emitter.onSuccess(bitmap) }
        } catch (e: Throwable) {
          emitter.onError(e)
        }
      }
    }
    
  }
  
}