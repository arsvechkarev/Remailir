package com.arsvechkarev.views

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.core.viewbuilding.Colors
import com.arsvechkarev.core.viewbuilding.Dimens
import com.arsvechkarev.core.viewbuilding.Dimens.ProgressBarSizeSmall
import com.arsvechkarev.core.viewbuilding.Styles.BaseTextView
import com.arsvechkarev.core.viewbuilding.Styles.ClickableTextView
import com.arsvechkarev.core.viewbuilding.TextSizes
import com.arsvechkarev.viewdsl.AccelerateDecelerateInterpolator
import com.arsvechkarev.viewdsl.DURATION_SHORT
import com.arsvechkarev.viewdsl.Ints.dp
import com.arsvechkarev.viewdsl.animateInvisible
import com.arsvechkarev.viewdsl.animateVisible
import com.arsvechkarev.viewdsl.atMost
import com.arsvechkarev.viewdsl.backgroundRoundRect
import com.arsvechkarev.viewdsl.circleRippleBackground
import com.arsvechkarev.viewdsl.exactly
import com.arsvechkarev.viewdsl.image
import com.arsvechkarev.viewdsl.invisible
import com.arsvechkarev.viewdsl.layoutLeftTop
import com.arsvechkarev.viewdsl.onClick
import com.arsvechkarev.viewdsl.size
import com.arsvechkarev.viewdsl.text
import com.arsvechkarev.viewdsl.textColor
import com.arsvechkarev.viewdsl.textSize
import com.arsvechkarev.viewdsl.unspecified

class Snackbar(context: Context) : ViewGroup(context) {
  
  private val progressBar get() = getChildAt(0) as SimpleProgressBar
  private val innerPadding = 8.dp
  private var opened = false
  
  val text get() = getChildAt(1) as TextView
  val textError get() = getChildAt(2) as TextView
  val buttonRetry get() = getChildAt(3) as TextView
  val imageClose get() = getChildAt(4) as ImageView
  
  val isOpened get() = opened
  
  init {
    val p = 4.dp
    clipToPadding = false
    backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
    setPadding(18.dp, 8.dp, 16.dp, 8.dp)
    addView(SimpleProgressBar(context))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply {
      apply(ClickableTextView(Colors.ErrorRipple, Colors.Dialog))
      textSize(TextSizes.H5)
      textColor(Colors.Error)
      text(R.string.text_retry_all_caps)
    })
    addView(ImageView(context).apply {
      setPadding(p, p, p, p)
      circleRippleBackground(Colors.Ripple)
      image(R.drawable.ic_cross)
      onClick { hide() }
    })
  }
  
  fun switchToLoadingMode() {
    text.animateVisible()
    progressBar.animateVisible()
    textError.animateInvisible()
    buttonRetry.invisible()
    imageClose.invisible()
  }
  
  fun switchToErrorMode() {
    text.animateInvisible()
    progressBar.animateInvisible()
    textError.animateVisible()
    buttonRetry.animateVisible()
    imageClose.animateVisible()
  }
  
  fun show() {
    if (opened) return
    opened = true
    animate().translationY(0f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(DURATION_SHORT)
        .withLayer()
        .start()
  }
  
  fun hide() {
    if (!opened) return
    opened = false
    animate().translationY(height * 1.8f)
        .setInterpolator(AccelerateDecelerateInterpolator)
        .setDuration(DURATION_SHORT)
        .withLayer()
        .start()
  }
  
  fun onRetryClicked(block: () -> Unit) {
    buttonRetry.onClick(block)
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = widthMeasureSpec.size
    progressBar.measure(exactly(ProgressBarSizeSmall), exactly(ProgressBarSizeSmall))
    imageClose.measure(exactly(ProgressBarSizeSmall), exactly(ProgressBarSizeSmall))
    buttonRetry.measure(unspecified(), heightMeasureSpec)
    val textMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        progressBar.measuredWidth
    text.measure(atMost(textMaxWidth), heightMeasureSpec)
    val textErrorMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        imageClose.measuredWidth - buttonRetry.measuredWidth
    textError.measure(atMost(textErrorMaxWidth), heightMeasureSpec)
    val height = maxOf(progressBar.measuredHeight, text.measuredHeight,
      buttonRetry.measuredHeight) + paddingTop + paddingBottom
    setMeasuredDimension(width, resolveSize(height, heightMeasureSpec))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    progressBar.layoutLeftTop(paddingStart, height / 2 - progressBar.measuredHeight / 2)
    text.layoutLeftTop(progressBar.right + innerPadding * 2, height / 2 - text.measuredHeight / 2)
    textError.layoutLeftTop(paddingStart, height / 2 - text.measuredHeight / 2)
    imageClose.layoutLeftTop(width - innerPadding - imageClose.measuredWidth,
      height / 2 - imageClose.measuredHeight / 2)
    buttonRetry.layoutLeftTop(imageClose.left - innerPadding / 4 - buttonRetry.measuredWidth,
      height / 2 - buttonRetry.measuredHeight / 2)
    if (!isOpened) {
      translationY = height * 1.5f
    }
  }
}