package core.viewdelegates

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import core.R

interface Toolbar {
  
  val includedToolbar: View?
    get() = null
  
  val textTitle: TextView
    get() = includedToolbar!!.findViewById(R.id.textTitle)
  
  val imageBack: ImageView
    get() = includedToolbar!!.findViewById(R.id.imageBack)
  
}