package com.arsvechkarev.views

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import config.DurationsConfigurator
import core.resources.Colors
import core.resources.Dimens
import core.resources.Dimens.CheckmarkHeightSmall
import core.resources.Dimens.CheckmarkWidthSmall
import core.resources.Dimens.MarginDefault
import core.resources.Dimens.MarginSmall
import core.resources.Dimens.ProgressBarSizeSmall
import core.resources.Styles.BaseTextView
import core.resources.Styles.ClickableTextView
import core.resources.TextSizes
import viewdsl.AccelerateDecelerateInterpolator
import viewdsl.atMost
import viewdsl.backgroundRoundRect
import viewdsl.circleRippleBackground
import viewdsl.exactly
import viewdsl.image
import viewdsl.invisible
import viewdsl.isVisible
import viewdsl.layoutLeftTop
import viewdsl.makeInvisible
import viewdsl.makeVisible
import viewdsl.makeVisibleAndWait
import viewdsl.onClick
import viewdsl.padding
import viewdsl.size
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.unspecified

class Snackbar(context: Context) : CoroutineViewGroup(context) {
  
  private val progressBar get() = getChildAt(0) as SimpleProgressBar
  private val innerPadding = MarginSmall
  private var opened = false
  
  private val textLoading get() = getChildAt(1) as TextView
  private val textFailure get() = getChildAt(2) as TextView
  private val textSuccess get() = getChildAt(3) as TextView
  private val buttonRetry get() = getChildAt(4) as TextView
  private val checkmarkView get() = getChildAt(5) as CheckmarkView
  private val imageHide get() = getChildAt(6) as ImageView
  
  init {
    clipToPadding = false
    backgroundRoundRect(Dimens.DefaultCornerRadius, Colors.Dialog)
    setPadding(MarginDefault, MarginSmall, MarginDefault, MarginSmall)
    addView(SimpleProgressBar(context))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply(BaseTextView))
    addView(TextView(context).apply {
      apply(ClickableTextView(Colors.ErrorRipple, Colors.Dialog))
      textSize(TextSizes.H5)
      textColor(Colors.Error)
      text(R.string.text_retry_all_caps)
    })
    addView(CheckmarkView(context).apply {
      invisible()
      val padding = MarginDefault
      setPadding(padding, padding, padding, padding)
    })
    addView(ImageView(context).apply {
      circleRippleBackground(Colors.Ripple)
      padding(Dimens.MarginVerySmall)
      image(R.drawable.ic_cross)
    })
  }
  
  fun switchToLoadingMode(animate: Boolean, loadingText: CharSequence) {
    setTextOnlyTo(textLoading, loadingText)
    textLoading.makeVisible(animate)
    progressBar.makeVisible(animate)
    textFailure.makeInvisible(animate)
    checkmarkView.makeInvisible(animate)
    textSuccess.makeInvisible(animate)
    buttonRetry.invisible()
    imageHide.invisible()
  }
  
  suspend fun switchToSuccessMode(
    animate: Boolean,
    infoText: CharSequence,
    onHideClick: () -> Unit
  ) {
    setTextOnlyTo(textSuccess, infoText)
    textLoading.makeInvisible(animate)
    progressBar.makeInvisible(animate)
    textFailure.makeInvisible(animate)
    buttonRetry.makeInvisible(animate)
    textSuccess.makeVisible(animate)
    imageHide.makeVisible(animate)
    imageHide.onClick(onHideClick)
    checkmarkView.makeVisibleAndWait(animate)
    checkmarkView.showCheckmarkAndWait(animate)
  }
  
  fun switchToFailureMode(
    animate: Boolean,
    failureText: CharSequence,
    onRetryClick: () -> Unit,
    onHideClick: () -> Unit
  ) {
    setTextOnlyTo(textFailure, failureText)
    textLoading.makeInvisible(animate)
    progressBar.makeInvisible(animate)
    checkmarkView.makeInvisible(animate)
    textSuccess.makeInvisible(animate)
    textFailure.makeVisible(animate)
    buttonRetry.makeVisible(animate)
    imageHide.makeVisible(animate)
    buttonRetry.onClick(onRetryClick)
    imageHide.onClick(onHideClick)
  }
  
  fun show(animate: Boolean) {
    if (opened) return
    opened = true
    if (!animate) {
      translationY = 0f
    } else {
      animate().translationY(0f)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .setDuration(DurationsConfigurator.DurationShort)
          .withLayer()
          .start()
    }
  }
  
  fun hide(animate: Boolean) {
    if (!opened) return
    opened = false
    val endTranslation = height * 1.8f
    if (!animate) {
      translationY = endTranslation
    } else {
      animate().translationY(endTranslation)
          .setInterpolator(AccelerateDecelerateInterpolator)
          .setDuration(DurationsConfigurator.DurationShort)
          .withLayer()
          .start()
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val width = widthMeasureSpec.size
    progressBar.measure(exactly(ProgressBarSizeSmall), exactly(ProgressBarSizeSmall))
    imageHide.measure(exactly(ProgressBarSizeSmall), exactly(ProgressBarSizeSmall))
    checkmarkView.measure(exactly(CheckmarkWidthSmall), exactly(CheckmarkHeightSmall))
    buttonRetry.measure(unspecified(), heightMeasureSpec)
    val textMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        progressBar.measuredWidth
    textLoading.measure(atMost(textMaxWidth), heightMeasureSpec)
    val textInfoWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        checkmarkView.measuredWidth
    textSuccess.measure(atMost(textInfoWidth), heightMeasureSpec)
    val textErrorMaxWidth = width - paddingStart - paddingEnd - innerPadding * 2 -
        imageHide.measuredWidth - buttonRetry.measuredWidth
    textFailure.measure(atMost(textErrorMaxWidth), heightMeasureSpec)
    val height = maxOf(progressBar.measuredHeight, textLoading.measuredHeight,
      textFailure.measuredHeight, buttonRetry.measuredHeight) + paddingTop + paddingBottom
    setMeasuredDimension(width, resolveSize(height, heightMeasureSpec))
  }
  
  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    progressBar.layoutLeftTop(paddingStart, height / 2 - progressBar.measuredHeight / 2)
    checkmarkView.layoutLeftTop(paddingStart, height / 2 - checkmarkView.measuredHeight / 2)
    textLoading.layoutLeftTop(progressBar.right + innerPadding * 2,
      height / 2 - textLoading.measuredHeight / 2)
    val textInfoTop = height / 2 - textSuccess.measuredHeight / 2
    if (checkmarkView.isVisible) {
      textSuccess.layoutLeftTop(checkmarkView.right + innerPadding * 2, textInfoTop)
    } else {
      textSuccess.layoutLeftTop(paddingStart, textInfoTop)
    }
    textFailure.layoutLeftTop(paddingStart, height / 2 - textFailure.measuredHeight / 2)
    imageHide.layoutLeftTop(width - innerPadding - imageHide.measuredWidth,
      height / 2 - imageHide.measuredHeight / 2)
    buttonRetry.layoutLeftTop(imageHide.left - innerPadding / 4 - buttonRetry.measuredWidth,
      height / 2 - buttonRetry.measuredHeight / 2)
    if (!opened) {
      translationY = height * 1.5f
    }
  }
  
  private fun setTextOnlyTo(textView: TextView, text: CharSequence) {
    textLoading.text("")
    textSuccess.text("")
    textFailure.text("")
    textView.text(text)
  }
}