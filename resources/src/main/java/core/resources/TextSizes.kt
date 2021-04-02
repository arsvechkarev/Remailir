package core.resources

import android.content.Context

object TextSizes {
  
  private val textSizes = FloatArray(8) { 0f }
  
  val Header: Float get() = textSizes[0]
  val H0: Float get() = textSizes[1]
  val H1: Float get() = textSizes[2]
  val H2: Float get() = textSizes[3]
  val H3: Float get() = textSizes[4]
  val H4: Float get() = textSizes[5]
  val H5: Float get() = textSizes[6]
  val H6: Float get() = textSizes[7]
  
  fun init(context: Context) {
    textSizes[0] = context.resources.getDimension(R.dimen.text_header)
    textSizes[1] = context.resources.getDimension(R.dimen.text_h0)
    textSizes[2] = context.resources.getDimension(R.dimen.text_h1)
    textSizes[3] = context.resources.getDimension(R.dimen.text_h2)
    textSizes[4] = context.resources.getDimension(R.dimen.text_h3)
    textSizes[5] = context.resources.getDimension(R.dimen.text_h4)
    textSizes[6] = context.resources.getDimension(R.dimen.text_h5)
    textSizes[7] = context.resources.getDimension(R.dimen.text_h6)
  }
}