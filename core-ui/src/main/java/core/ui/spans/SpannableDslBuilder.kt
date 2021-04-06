package core.ui.spans

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import navigation.BaseScreen

/**
 * Creates char sequence with different spannables. For instance:
 *
 * val text = spannable {
 *   +"This is a "
 *   +"very".withFont(Typeface.BOLD)
 *   +" important message"
 * }
 *
 * Result: "This is a **very** important message"
 */
fun spannable(dslBuilder: SpannableDslBuilder.() -> Unit): CharSequence {
  return SpannableDslBuilder().apply(dslBuilder).toText()
}

class SpannableDslBuilder {
  
  private val builder = SpannableStringBuilder()
  
  operator fun CharSequence.unaryPlus() {
    builder.append(this)
  }
  
  operator fun Unit.unaryPlus() {
    // Overriding unary plus for Unit class, allows putting "+" in front of every function
    // that returns Unit. Now when we call builders, like withFont(Typeface) we can put "+"
    // in front of them and create feeling of fluent api
  }
  
  fun CharSequence.withFont(font: Typeface) {
    builder.append(this, CustomFontSpan(font), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
  }
  
  fun toText(): CharSequence = builder
}