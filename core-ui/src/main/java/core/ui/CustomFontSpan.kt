package core.ui

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.MetricAffectingSpan

class CustomFontSpan(private val typeface: Typeface) : MetricAffectingSpan() {
  
  override fun updateDrawState(ds: TextPaint) {
    ds.typeface = typeface
  }
  
  override fun updateMeasureState(paint: TextPaint) {
    paint.typeface = typeface
  }
}