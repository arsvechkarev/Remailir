package com.arsvechkarev.core

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.viewdsl.childView
import moxy.MvpAppCompatFragment

abstract class BaseFragment(
  private val layoutResId: Int = 0
) : MvpAppCompatFragment(layoutResId) {
  
  private val viewsCache = HashMap<String, View>()
  
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return if (layoutResId == 0) {
      buildLayout() ?: super.onCreateView(inflater, container, savedInstanceState)
    } else
      super.onCreateView(inflater, container, savedInstanceState)
  }
  
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    onInit()
    checkForOrientation(requireContext().resources.configuration)
  }
  
  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    checkForOrientation(newConfig)
  }
  
  private fun checkForOrientation(newConfig: Configuration) {
    if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
      onOrientationBecamePortrait()
    } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
      onOrientationBecameLandscape()
    }
  }
  
  abstract fun onInit()
  
  open fun buildLayout(): View? = null
  
  open fun onAppearedOnScreen() = Unit
  
  open fun onNetworkAvailable() = Unit
  
  open fun onDrawerOpened() = Unit
  
  open fun onDrawerClosed() = Unit
  
  open fun onOrientationBecamePortrait() = Unit
  
  open fun onOrientationBecameLandscape() = Unit
  
  /**
   * If returns true, then navigator is allowed to pop this fragment. If returns false, then
   * fragment is not popped from back stack
   */
  open fun allowBackPress(): Boolean = true
  
  @Suppress("UNCHECKED_CAST")
  fun view(tag: String): View {
    if (viewsCache[tag] == null) {
      viewsCache[tag] = requireView().childView(tag)
    }
    return viewsCache.getValue(tag)
  }
  
  @Suppress("UNCHECKED_CAST")
  fun <T : View> viewAs(tag: String) = view(tag) as T
  
  fun imageView(tag: String) = viewAs<ImageView>(tag)
  
  fun textView(tag: String) = viewAs<TextView>(tag)
  
  fun editText(tag: String) = viewAs<EditText>(tag)
}