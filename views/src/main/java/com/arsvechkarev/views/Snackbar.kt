package com.arsvechkarev.views

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import core.resources.Dimens.CheckmarkHeightSmall
import core.resources.Dimens.CheckmarkWidthSmall
import core.resources.Dimens.ProgressBarSizeSmall
import core.resources.Styles.BaseTextView
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.DURATION_SHORT
import viewdsl.Ints.dp
import viewdsl.animateInvisible
import viewdsl.animateVisible
import viewdsl.atMost
import viewdsl.backgroundRoundRect
import viewdsl.circleRippleBackground
import viewdsl.exactly
import viewdsl.image
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutLeftTop
import viewdsl.onClick
import viewdsl.size
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.unspecified

class Snackbar(context: Context) : ViewGroup(context) {
  
  private val progressBar get() = getChildAt(0) as SimpleProgressBar
  private val innerPadding = 8.dp
  private var opened = false
  
  val textLoading get() = getChildAt(1) as TextView
  val textError get() = getChildAt(2) as TextView
  val textInfo get() = getChildAt(3) as TextView
  val buttonRetry get() = getChildAt(4) as TextView
  val checkmarkView get() = getChildAt(5) as CheckmarkView
  val imageClose get() = getChildAt(6) as ImageView
  
  val isOpened get() = opened
  
  init {
    val p = 4.dp
    clipToPadding = false
    backgroundRoundRect(core.resources.Dimens.DefaultCornerRadius, core.resources.Colors.Dialog)
    setPadding(18.dp, 8.dp, 16.dp, 8.dp)
    addView(SimpleProgressBar(context))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply {
      apply(ClickableTextView(core.resources.Colors.ErrorRipple, core.resources.Colors.Dialog))
      textSize(TextSizes.H5)
      textColor(core.resources.Colors.Error)
      text(R.string.text_retry_all_caps)
    })
    addView(CheckmarkView(context).apply {
      invisible()
      val padding = 16.dp
      setPadding(padding, padding, padding, padding)
    })
    addView(ImageView(context).apply {
      setPadding(p, p, p, p)
      circleRippleBackground(core.resources.Colors.Ripple)
      image(R.drawable.ic_cross)
      onClick { hide() }
    })
  }
  
  fun switchToLoadingMode() {
    textError.text("")
    textInfo.text("")
    textLoading.animateVisible()
    progressBar.animateVisible()
    textError.animateInvisible()
    checkmarkView.animateInvisible()
    textInfo.animateInvisible()
    buttonRetry.invisible()
    imageClose.invisible()
  }
  
  fun switchToErrorMode() {
    textInfo.text("")
    textLoading.text("")
    textLoading.animateInvisible()
    progressBar.animateInvisible()
    checkmarkView.animateInvisible()
    textInfo.animateInvisible()
    textError.animateVisible()
    buttonRetry.animateVisible()
    imageClose.animateVisible()
  }
  
  fun switchToInfoMode() {
    textError.text("")
    textLoading.text("")
    textLoading.animateInvisible()
    progressBar.animateInvisible()
    textError.animateInvisible()
    buttonRetry.animateInvisible()
    textInfo.animateVisible()
    checkmarkView.animateVisible(andThen = {
      if (isAttachedToWindow) {
        checkmarkView.animateCheckmark()
      }
    })
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
    checkmarkView.measure(exactly(CheckmarkWidthSmall), exactly(CheckmarkHeightSmall))
    buttonRetry.measure(unspecified(), heightMeasureSpec)
    val textMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        progressBar.measuredWidth
    textLoading.measure(atMost(textMaxWidth), heightMeasureSpec)
    val textInfoWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        checkmarkView.measuredWidth
    textInfo.measure(atMost(textInfoWidth), heightMeasureSpec)
    val textErrorMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        imageClose.measuredWidth - buttonRetry.measuredWidth
    textError.measure(atMost(textErrorMaxWidth), heightMeasureSpec)
    val height = maxOf(progressBar.measuredHeight, textLoading.measuredHeight,
      textError.measuredHeight, buttonRetry.measuredHeight) + paddingTop + paddingBottom
    setMeasuredDimension(width, resolveSize(height, heightMeasureSpec))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    progressBar.layoutLeftTop(paddingStart, height / 2 - progressBar.measuredHeight / 2)
    checkmarkView.layoutLeftTop(paddingStart, height / 2 - checkmarkView.measuredHeight / 2)
    textLoading.layoutLeftTop(progressBar.right + innerPadding * 2,
      height / 2 - textLoading.measuredHeight / 2)
    val textInfoTop = height / 2 - textInfo.measuredHeight / 2
    if (checkmarkView.isVisible) {
      textInfo.layoutLeftTop(checkmarkView.right + innerPadding * 2, textInfoTop)
    } else {
      textInfo.layoutLeftTop(paddingStart, textInfoTop)
    }
    textError.layoutLeftTop(paddingStart, height / 2 - textError.measuredHeight / 2)
    imageClose.layoutLeftTop(width - innerPadding - imageClose.measuredWidth,
      height / 2 - imageClose.measuredHeight / 2)
    buttonRetry.layoutLeftTop(imageClose.left - innerPadding / 4 - buttonRetry.measuredWidth,
      height / 2 - buttonRetry.measuredHeight / 2)
    if (!isOpened) {
      translationY = height * 1.5f
    }
  }
}