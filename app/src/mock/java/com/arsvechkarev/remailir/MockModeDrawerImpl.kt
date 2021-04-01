package com.arsvechkarev.remailir

import android.graphics.Canvas
import android.graphics.Color
import com.arsvechkarev.core.MockModeLabelDrawer
import com.arsvechkarev.core.extenstions.Paint
import com.arsvechkarev.core.extenstions.TextPaint
import com.arsvechkarev.core.extenstions.execute
import com.arsvechkarev.core.viewbuilding.Fonts
import com.arsvechkarev.views.boringLayoutOf

object MockModeDrawerImpl : MockModeLabelDrawer {
  
  private val paint = Paint(Color.RED)
  private val textPaint = TextPaint(textSize = 60f, color = Color.WHITE, font = Fonts.SegoeUiBold)
  
  private val textLayout = boringLayoutOf(textPaint, "MOCK MODE")
  
  override fun drawLabel(canvas: Canvas) {
    val centerX = canvas.width / 2
    val width = 400f
    val height = 100f
    canvas.drawRect(centerX - width / 2, 0f, centerX + width / 2, height, paint)
    canvas.execute {
      translate(canvas.width / 2f, 0f)
      textLayout.draw(canvas)
    }
  }
}