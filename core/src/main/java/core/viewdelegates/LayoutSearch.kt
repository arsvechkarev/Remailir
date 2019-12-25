package core.viewdelegates

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import core.R

interface LayoutSearch {
  
  val includedSearch: View
  
  val editTextSearch: EditText
    get() = includedSearch.findViewById(R.id.editTextSearch)
  
  val imageBack: ImageView
    get() = includedSearch.findViewById(R.id.imageBack)
  
  val imageCross: ImageView
    get() = includedSearch.findViewById(R.id.imageCross)
}