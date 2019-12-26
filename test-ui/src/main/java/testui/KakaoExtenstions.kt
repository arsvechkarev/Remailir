package testui

import androidx.annotation.StringRes
import com.agoda.kakao.common.views.KBaseView
import com.agoda.kakao.edit.KEditText
import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KTextView

inline fun <reified T : Screen<T>> screen(): T {
  return T::class.java
    .newInstance()
}

fun <T> assertNotDisplayed(vararg views: KBaseView<T>) {
  views.forEach { it.isNotDisplayed() }
}

fun <T> assertDisplayed(vararg views: KBaseView<T>) {
  views.forEach { it.isDisplayed() }
}

infix fun <T : KBaseView<T>> T.isVisibleAnd(block: T.() -> Unit) {
  apply {
    isVisible()
    block()
  }
}

infix fun KTextView.isVisibleAndHasText(text: String) {
  apply {
    isVisible()
    hasText(text)
  }
}

infix fun KTextView.isVisibleAndHasText(@StringRes textResId: Int) {
  apply {
    isVisible()
    hasText(textResId)
  }
}

infix fun KEditText.isDisplayedAndHasHint(text: String) {
  apply {
    isDisplayed()
    hasHint(text)
  }
}

infix fun KEditText.isDisplayedAndHasHint(@StringRes textResId: Int) {
  apply {
    isDisplayed()
    hasHint(textResId)
  }
}

infix fun KEditText.clearAndTypeText(text: String) {
  apply {
    clearText()
    typeText(text)
  }
}