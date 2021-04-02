package com.arsvechkarev.remailir

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.WHITE
import com.arsvechkarev.views.utils.Paint
import com.arsvechkarev.views.utils.TextPaint
import com.arsvechkarev.views.utils.boringLayoutOf
import com.arsvechkarev.views.utils.execute
import core.MockModeLabelDrawer
import core.resources.Fonts.SegoeUiBold

object MockModeDrawerImpl : MockModeLabelDrawer {
  
  // Set this properties as lazy, otherwise application freezes on start
  private val paint by lazy { Paint(Color.RED) }
  private val textPaint by lazy { TextPaint(textSize = 60f, color = WHITE, font = SegoeUiBold) }
  private val textLayout by lazy { boringLayoutOf(textPaint, "MOCK MODE") }
  
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