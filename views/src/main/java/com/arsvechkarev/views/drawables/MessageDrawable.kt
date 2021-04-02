package com.arsvechkarev.views.drawables

import android.graphics.Canvas
import android.graphics.Path
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.LEFT_CORNER
import com.arsvechkarev.views.drawables.MessageDrawable.MessageType.RIGHT_CORNER
import com.arsvechkarev.views.utils.Paint
import com.arsvechkarev.views.utils.execute
import core.resources.Dimens.MessageStickingDistance
import viewdsl.Floats.dp

class MessageDrawable(
  color: Int,
  private val messageType: MessageType
) : BaseDrawable() {
  
  private val paint = Paint(color)
  private val path = Path()
  private val cornerRadius = 4.dp
  private val stickingDistance = MessageStickingDistance.toFloat()
  
  override fun draw(canvas: Canvas) {
    val width = bounds.width().toFloat()
    val height = bounds.height().toFloat()
    createPathIsEmpty(width)
    canvas.execute {
      if (messageType == LEFT || messageType == LEFT_CORNER) {
        canvas.scale(-1f, 1f, width / 2f, height / 2f)
      }
      canvas.drawRoundRect(
        0f, 0f, width - stickingDistance, height,
        cornerRadius, cornerRadius, paint
      )
      if (messageType == LEFT_CORNER || messageType == RIGHT_CORNER) {
        canvas.drawPath(path, paint)
      }
    }
  }
  
  private fun createPathIsEmpty(width: Float) {
    if (path.isEmpty) {
      val smallOffset = cornerRadius / 6f
      with(path) {
        reset()
        moveTo(width - cornerRadius * 2 - stickingDistance, 0f)
        lineTo(width - smallOffset, 0f)
        quadTo(
          width, smallOffset, width - smallOffset, smallOffset * 2
        )
        quadTo(
          width - stickingDistance, smallOffset * 2.5f,
          width - stickingDistance * 1.2f, stickingDistance * 2f
        )
        close()
      }
    }
  }
  
  enum class MessageType {
    LEFT_CORNER, RIGHT_CORNER, LEFT, RIGHT
  }
}