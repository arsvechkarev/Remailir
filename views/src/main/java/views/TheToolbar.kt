package views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class TheToolbar @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
  
  private var hasSearch = false
  private var title = ""
  
  private val textTitle by lazy { findViewById<TextView>(R.id.textTitle) }
  private val imageBack by lazy { findViewById<ImageView>(R.id.imageBack) }
  private val imageSearch by lazy { findViewById<ImageView>(R.id.imageSearch) }
  
  init {
    inflate(context, R.layout.the_toolbar, this)
    val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.TheToolbar, 0, 0)
    try {
      title = typedArray.getString(R.styleable.TheToolbar_title) ?: ""
      hasSearch = typedArray.getBoolean(R.styleable.TheToolbar_hasSearch, false)
    } finally {
      typedArray.recycle()
    }
    textTitle.text = title
  }
  
  
}