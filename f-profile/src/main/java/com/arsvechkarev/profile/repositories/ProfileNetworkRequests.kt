package com.arsvechkarev.profile.repositories

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import core.strings.DEFAULT_IMG_URL
import io.reactivex.Maybe
import javax.inject.Inject

class ProfileNetworkRequests @Inject constructor(
  private val picasso: Picasso
) {
  
  fun loadProfileImage(imageUrl: String): Maybe<Bitmap> {
    return Maybe.create { emitter ->
      if (imageUrl == DEFAULT_IMG_URL) {
        emitter.onComplete()
      } else {
        picasso.load(imageUrl).into(object : Target {
          override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
          
          override fun onBitmapFailed(e: Exception, errorDrawable: Drawable?) {
            emitter.onError(e)
          }
          
          override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
            emitter.onSuccess(bitmap)
          }
        })
      }
    }
    
  }
  
}