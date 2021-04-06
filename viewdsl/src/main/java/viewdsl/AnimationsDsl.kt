@file:Suppress("UsePropertyAccessSyntax")

package viewdsl

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Animatable
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import config.DurationsConfigurator
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val AccelerateDecelerateInterpolator = AccelerateDecelerateInterpolator()
val OvershootInterpolator = OvershootInterpolator()

fun Animator.startIfNotRunning() {
  if (!isRunning) start()
}

fun Animator.cancelIfRunning() {
  if (isRunning) cancel()
}

fun Animatable.startIfNotRunning() {
  if (!isRunning) start()
}

fun Animatable.stopIfRunning() {
  if (isRunning) stop()
}

fun Animator.doOnEnd(block: () -> Unit) {
  addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      block()
      removeListener(this)
    }
  })
}

suspend fun View.makeVisibleAndWait(animate: Boolean, duration: Long = DurationsConfigurator.DurationDefault) {
  if (!animate) {
    visible()
  } else {
    suspendCoroutine<Unit> { continuation ->
      animateVisible(duration = duration, andThen = { continuation.resume(Unit) })
    }
  }
}

suspend fun View.animateInvisibleSuspend(duration: Long = DurationsConfigurator.DurationDefault) {
  suspendCoroutine<Unit> { continuation ->
    animateInvisible(duration = duration, andThen = { continuation.resume(Unit) })
  }
}

fun View.animateVisible(andThen: () -> Unit = {}, duration: Long = DurationsConfigurator.DurationDefault) {
  alpha = 0f
  visible()
  animate().alpha(1f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction(andThen)
      .start()
}

fun View.animateInvisible(andThen: () -> Unit = {}, duration: Long = DurationsConfigurator.DurationDefault) {
  animate().alpha(0f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        invisible()
        andThen()
      }
      .start()
}

fun View.animateGone(andThen: () -> Unit = {}, duration: Long = DurationsConfigurator.DurationDefault) {
  animate().alpha(0f).setDuration(duration)
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        gone()
        andThen()
      }
      .start()
}

fun View.animateSlideFromRight(duration: Long = DurationsConfigurator.DurationShort) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  alpha = 0f
  translationX = minOf(width, height) / 8f
  visible()
  animate().alpha(1f)
      .translationX(0f)
      .setDuration(duration)
      .withLayer()
      .setInterpolator(AccelerateDecelerateInterpolator)
      .start()
}

fun View.animateSlideToRight(duration: Long = DurationsConfigurator.DurationShort) {
  val width = context.resources.displayMetrics.widthPixels
  val height = context.resources.displayMetrics.heightPixels
  val dx = minOf(width, height) / 8f
  animate().alpha(0f)
      .translationX(dx)
      .setDuration(duration)
      .withLayer()
      .setInterpolator(AccelerateDecelerateInterpolator)
      .withEndAction {
        gone()
        translationX = 0f
      }
      .start()
}

fun animateVisible(vararg views: View, andThen: () -> Unit = {}) {
  var andThenPosted = false
  for (view in views) {
    if (!andThenPosted) {
      view.animateVisible(andThen)
      andThenPosted = true
    } else {
      view.animateVisible()
    }
  }
}

fun animateInvisible(vararg views: View, andThen: () -> Unit = {}) {
  var andThenPosted = false
  for (view in views) {
    if (!andThenPosted) {
      view.animateInvisible(andThen)
      andThenPosted = true
    } else {
      view.animateInvisible()
    }
  }
}