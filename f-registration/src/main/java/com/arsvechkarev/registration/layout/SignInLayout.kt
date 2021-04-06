package com.arsvechkarev.registration.layout

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.arsvechkarev.registration.R
import com.arsvechkarev.registration.layout.RegistrationDimens.EditTextPadding
import com.arsvechkarev.registration.layout.RegistrationDimens.MarginBottom
import com.arsvechkarev.registration.layout.RegistrationDimens.MarginHorizontal
import com.arsvechkarev.registration.layout.RegistrationDimens.MarginHorizontalBig
import com.arsvechkarev.registration.layout.RegistrationDimens.MarginTop
import com.arsvechkarev.registration.layout.RegistrationDimens.MarginTopSmall
import com.arsvechkarev.views.CheckmarkView
import com.arsvechkarev.views.MaterialProgressBar
import core.resources.Dimens.CheckmarkHeight
import core.resources.Dimens.CheckmarkWidth
import core.resources.Dimens.FailureLayoutImageSize
import core.resources.Dimens.FailureLayoutTextPadding
import core.resources.Dimens.IconLogoSize
import core.resources.Dimens.ProgressBarSizeBig
import core.resources.Styles.ClickableButton
import core.resources.TextSizes
import viewdsl.Ints.dp
import viewdsl.Size.Companion.MatchParent
import viewdsl.Size.Companion.WrapContent
import viewdsl.addView
import viewdsl.children
import viewdsl.exactly
import viewdsl.font
import viewdsl.gone
import viewdsl.gravity
import viewdsl.heightWithMargins
import viewdsl.image
import viewdsl.invisible
import viewdsl.layoutAroundPoint
import viewdsl.layoutLeftTop
import viewdsl.marginEnd
import viewdsl.marginStart
import viewdsl.marginTop
import viewdsl.margins
import viewdsl.padding
import viewdsl.paddings
import viewdsl.size
import viewdsl.tag
import viewdsl.text
import viewdsl.textColor
import viewdsl.textSize
import viewdsl.unspecified
import viewdsl.visible
import viewdsl.withViewBuilder

@SuppressLint("ViewConstructor")
class SignInLayout(context: Context) : ViewGroup(context) {
  
  private var diff = -1
  
  private val iconLogo get() = getChildAt(0) as ImageView
  private val textDescription get() = getChildAt(1) as TextView
  private val editText get() = getChildAt(2) as EditText
  private val textEditTextError get() = getChildAt(3) as TextView
  private val textLinkWasSent get() = getChildAt(4) as TextView
  private val textTimer get() = getChildAt(5) as TextView
  private val buttonOpenEmail get() = getChildAt(6) as TextView
  private val buttonSignIn get() = getChildAt(7) as SignInButton
  private val layoutLoading get() = getChildAt(8) as LinearLayout
  private val layoutError get() = getChildAt(9) as LinearLayout
  
  private val tempTextView = TextView(context)
  private val tempLayoutParams = LayoutParams(0, 0)
  
  init {
    withViewBuilder {
      ImageView(IconLogoSize, IconLogoSize) {
        tag(ImageLogo)
        image(R.drawable.image_msg)
      }
      TextView(MatchParent, WrapContent) {
        tag(TextDescription)
        gravity(Gravity.CENTER)
        margins(start = MarginHorizontal, end = MarginHorizontal, top = MarginTop)
        textSize(TextSizes.H4)
        text(R.string.text_enter_your_email)
        font(core.resources.Fonts.SegoeUi)
      }
      child<EditText, MarginLayoutParams>(MatchParent, WrapContent) {
        tag(EditTextTag)
        margins(start = MarginHorizontal, end = MarginHorizontal, top = MarginTop)
        font(core.resources.Fonts.SegoeUi)
        textSize(TextSizes.H3)
        padding(EditTextPadding)
        setSingleLine()
        isEnabled = false
      }
      TextView(MatchParent, WrapContent) {
        tag(TextEditTextError)
        invisible()
        margins(start = MarginHorizontalBig, end = MarginHorizontalBig, top = MarginTop)
        textColor(core.resources.Colors.Error)
        textSize(TextSizes.H4)
        font(core.resources.Fonts.SegoeUi)
      }
      TextView(MatchParent, WrapContent) {
        tag(TextLinkWasSent)
        invisible()
        gravity(Gravity.CENTER)
        margins(start = MarginHorizontal,
          end = MarginHorizontal)
        textSize(TextSizes.H3)
        text(R.string.error_email_sent)
        font(core.resources.Fonts.SegoeUi)
      }
      TextView(MatchParent, WrapContent) {
        tag(TextTimer)
        invisible()
        textColor(core.resources.Colors.TextSecondary)
        gravity(Gravity.CENTER)
        margins(top = MarginTopSmall, start = MarginHorizontal, end = MarginHorizontal)
        textSize(TextSizes.H4)
        font(core.resources.Fonts.SegoeUi)
      }
      TextView(WrapContent, WrapContent, style = ClickableButton()) {
        tag(ButtonOpenEmailApp)
        invisible()
        text(R.string.text_open_email_app)
        margins(top = MarginTop, start = MarginHorizontal, end = MarginHorizontal)
      }
      child<SignInButton, MarginLayoutParams>(MatchParent, WrapContent) {
        tag(ButtonSignIn)
        margins(start = MarginHorizontal, end = MarginHorizontal, bottom = MarginBottom)
      }
      VerticalLayout(MatchParent, WrapContent) {
        tag(LayoutLoading)
        invisible()
        gravity(Gravity.CENTER)
        TextView(WrapContent, WrapContent, style = core.resources.Styles.BoldTextView) {
          tag(TextLoading)
          text(R.string.text_verifying_link)
          padding(24.dp)
          textSize(TextSizes.H1)
        }
        FrameLayout(WrapContent, WrapContent) {
          child<CheckmarkView>(CheckmarkWidth, CheckmarkHeight) {
            invisible()
            tag(Checkmark)
          }
          addView {
            MaterialProgressBar(context, core.resources.Colors.Accent,
              MaterialProgressBar.Thickness.THICK).apply {
              tag(ProgressBarTag)
              size(ProgressBarSizeBig, ProgressBarSizeBig)
            }
          }
        }
      }
      VerticalLayout(MatchParent, MatchParent) {
        tag(LayoutError)
        invisible()
        gravity(Gravity.CENTER)
        ImageView(FailureLayoutImageSize, FailureLayoutImageSize) {
          image(R.drawable.image_unknown_error)
          margins(bottom = FailureLayoutTextPadding)
        }
        TextView(WrapContent, WrapContent, style = core.resources.Styles.BoldTextView) {
          tag(TextError)
          gravity(Gravity.CENTER)
          paddings(
            start = FailureLayoutTextPadding,
            end = FailureLayoutTextPadding,
            bottom = FailureLayoutTextPadding
          )
          textSize(TextSizes.H2)
          text(R.string.error_email_link_expired)
        }
        TextView(WrapContent, WrapContent, style = ClickableButton(
          colorStart = core.resources.Colors.ErrorGradientStart,
          colorEnd = core.resources.Colors.ErrorGradientEnd,
        )) { tag(ButtonRetry) }
      }
    }
  }
  
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    for (child in children) {
      if (child.layoutParams is MarginLayoutParams) {
        measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
      } else {
        measureChild(child, widthMeasureSpec, heightMeasureSpec)
      }
    }
  }
  
  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    val screenHeight = context.resources.displayMetrics.heightPixels
    val newDiff = screenHeight - height
    if (height < screenHeight * 0.8f || diff < newDiff && newDiff > screenHeight * 0.2f) {
      // Keyboard opened
      layoutKeyboardOpened()
    } else {
      // Keyboard closed
      layoutKeyboardClosed()
    }
    diff = newDiff
    layoutLoading.layoutAroundPoint(width / 2, height / 2)
    layoutError.layoutAroundPoint(width / 2, height / 2)
  }
  
  private fun layoutKeyboardOpened() {
    var mainViewsHeight = iconLogo.heightWithMargins() +
        textDescription.heightWithMargins() +
        editText.heightWithMargins() +
        getTextEditTextErrorHeight() + MarginTop
    val distToBottom = height / 2f - mainViewsHeight / 2f
    val buttonHeight = buttonSignIn.heightWithMargins()
    if (distToBottom < buttonHeight) {
      mainViewsHeight -= iconLogo.heightWithMargins()
    }
    val top = height / 2 - mainViewsHeight / 2
    val iconBottom = if (distToBottom > buttonHeight) {
      iconLogo.visible()
      iconLogo.layoutAroundPoint(width / 2, top + iconLogo.measuredHeight / 2)
      iconLogo.bottom
    } else {
      iconLogo.gone()
      top
    }
    textDescription.layoutLeftTop(textDescription.marginStart,
      iconBottom + textDescription.marginTop)
    editText.layoutLeftTop(editText.marginStart,
      textDescription.bottom + editText.marginTop)
    val textErrorTop = editText.bottom + textEditTextError.marginTop
    textEditTextError.layout(
      textEditTextError.marginStart,
      textErrorTop,
      textEditTextError.marginStart + textEditTextError.measuredWidth,
      textErrorTop + getTextEditTextErrorHeight(),
    )
    buttonSignIn.layoutLeftTop(buttonSignIn.marginStart,
      height - buttonSignIn.heightWithMargins())
  }
  
  private fun layoutKeyboardClosed() {
    val mainViewsHeight = iconLogo.heightWithMargins() +
        textDescription.heightWithMargins() +
        editText.heightWithMargins() +
        getTextEditTextErrorHeight() + MarginTop +
        textLinkWasSent.heightWithMargins() +
        textTimer.heightWithMargins() +
        buttonOpenEmail.heightWithMargins()
    val top = height / 2 - mainViewsHeight / 2
    if (editText.visibility == View.VISIBLE) {
      iconLogo.visible()
    }
    iconLogo.layoutAroundPoint(width / 2, top + iconLogo.measuredHeight / 2)
    textDescription.layoutLeftTop(textDescription.marginStart,
      iconLogo.bottom + textDescription.marginTop)
    editText.layoutLeftTop(editText.marginStart,
      textDescription.bottom + editText.marginTop)
    val textErrorTop = editText.bottom + textEditTextError.marginTop
    textEditTextError.layout(
      textEditTextError.marginStart,
      textErrorTop,
      textEditTextError.marginStart + textEditTextError.measuredWidth,
      textErrorTop + getTextEditTextErrorHeight(),
    )
    textLinkWasSent.layoutLeftTop(textLinkWasSent.marginStart,
      textEditTextError.bottom + textLinkWasSent.marginTop)
    textTimer.layoutLeftTop(textTimer.marginStart,
      textLinkWasSent.bottom + textTimer.marginTop)
    val y = textTimer.bottom + buttonOpenEmail.marginTop +
        buttonOpenEmail.measuredHeight / 2
    buttonOpenEmail.layoutAroundPoint(width / 2, y)
    buttonSignIn.layoutLeftTop(buttonSignIn.marginStart,
      height - buttonSignIn.heightWithMargins())
  }
  
  private fun getTextEditTextErrorHeight(): Int {
    val width = width - textEditTextError.marginStart - textEditTextError.marginEnd
    tempTextView.textSize(textEditTextError.textSize)
    tempTextView.typeface = textEditTextError.typeface
    tempTextView.layoutParams = tempLayoutParams
    tempTextView.layoutParams.width = width
    tempTextView.layoutParams.height = WRAP_CONTENT
    // Set the longest possible text
    tempTextView.text(R.string.error_username_contains_prohibited_symbols)
    tempTextView.measure(exactly(width), unspecified())
    return tempTextView.measuredHeight
  }
  
  companion object {
    
    const val ImageLogo = "ImageLogo"
    const val EditTextTag = "EditTextTag"
    const val ButtonSignIn = "ButtonSignIn"
    const val TextTimer = "TextTimer"
    const val ButtonOpenEmailApp = "ButtonOpenEmailApp"
    const val TextEditTextError = "TextEditTextError"
    const val TextDescription = "TextDescription"
    const val TextLinkWasSent = "TextLinkWasSent"
    
    const val LayoutLoading = "LayoutLoading"
    const val LayoutError = "LayoutError"
    const val TextError = "TextError"
    const val ButtonRetry = "ButtonRetry"
    const val ProgressBarTag = "ProgressBarTag"
    const val Checkmark = "Checkmark"
    const val TextLoading = "TextLoading"
  }
}