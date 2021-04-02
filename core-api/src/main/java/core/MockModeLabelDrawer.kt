package core

import android.graphics.Canvas

/**
 * When application is in mock mode (not in debug or release), draws a label on top of all views
 * telling it is in mock mode. In debug and release mode this doesn't draw anything.
 */
interface MockModeLabelDrawer {
  
  /**
   * Draws a label on [canvas]
   */
  fun drawLabel(canvas: Canvas)
}

object MockModeDrawerHolder {
  
  private var _mockModeLabelDrawer: MockModeLabelDrawer = object : MockModeLabelDrawer {
    override fun drawLabel(canvas: Canvas) {
      // Does nothing by default
    }
  }
  
  fun setMockModeDrawer(mockModeLabelDrawer: MockModeLabelDrawer) {
    _mockModeLabelDrawer = mockModeLabelDrawer
  }
  
  val mockModeDrawer get() = _mockModeLabelDrawer
}