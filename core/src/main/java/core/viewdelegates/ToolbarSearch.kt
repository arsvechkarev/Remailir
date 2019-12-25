package core.viewdelegates

import android.widget.ImageView
import core.R

interface ToolbarSearch : Toolbar {
  
  val imageSearch: ImageView
    get() = includedToolbar.findViewById(R.id.imageSearch)
  
}