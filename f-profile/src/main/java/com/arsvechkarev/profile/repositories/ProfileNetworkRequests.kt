package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import core.strings.DEFAULT_IMG_URL
import io.reactivex.Maybe
import log.Loggable
import log.log
import javax.inject.Inject

class ProfileNetworkRequests @Inject constructor(
  private val picasso: Picasso
) : Loggable {
  
  override val logTag = "ProfileStuff"
  
  fun loadProfileImage(imageUrl: String): Maybe<Bitmap> {
    return Maybe.create { emitter ->
      if (imageUrl == DEFAULT_IMG_URL) {
        log { "url is default, return completion" }
        emitter.onComplete()
      } else {
        log { "loading from url" }
        picasso.load(imageUrl).into(object : Target {
          override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
          
          override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
            log { "failed while loading with picasso" }
            emitter.onError(e)
          }
          
          override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
            log { "loaded successfully with picasso" }
            emitter.onSuccess(bitmap)
          }
        })
      }
    }
    
  }
  
}