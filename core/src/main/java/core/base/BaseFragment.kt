package core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
  
  abstract val layout: Int
  
  open fun onInit() {}
  
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(layout, container, false)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    onInit()
  }
  
}