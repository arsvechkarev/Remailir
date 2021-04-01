package com.arsvechkarev.core.navigation

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.arsvechkarev.viewdsl.childView
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpView

abstract class ViewPagerScreen : MvpDelegateHolder, MvpView {
  
  private var _mvpDelegate: MvpDelegate<out ViewPagerScreen>? = null
  
  @PublishedApi
  internal val viewsCache = HashMap<Any, View>()
  
  internal var _view: View? = null
  
  val view get() = _view
  
  val context get() = _view?.context
  
  val viewNonNull: View get() = view!!
  
  val contextNonNull get() = context!!
  
  override fun getMvpDelegate(): MvpDelegate<*> {
    return _mvpDelegate ?: run {
      _mvpDelegate = MvpDelegate(this)
      _mvpDelegate!!
    }
  }
  
  abstract fun buildView(context: Context): View
  
  internal fun createLayout(context: Context) {
    _view = buildView(context)
  }
  
  internal fun onCreateDelegate() {
    mvpDelegate.onCreate()
    mvpDelegate.onAttach()
  }
  
  internal fun onDestroyDelegate() {
    mvpDelegate.onDetach()
    mvpDelegate.onDestroyView()
    mvpDelegate.onDestroy()
  }
  
  open fun onBackPressed(): Boolean = false
  
  open fun onInit() = Unit
  
  open fun onRelease() = Unit
  
  open fun onAppearedOnScreen() = Unit
  
  open fun onOrientationBecamePortrait() = Unit
  
  open fun onOrientationBecameLandscape() = Unit
  
  @Suppress("UNCHECKED_CAST")
  fun view(tag: Any): View {
    if (viewsCache[tag] == null) {
      val view = if (tag is Int) {
        viewNonNull.findViewById<View>(tag)
      } else {
        viewNonNull.childView(tag)
      }
      viewsCache[tag] = view
    }
    return viewsCache.getValue(tag)
  }
  
  @Suppress("UNCHECKED_CAST")
  inline fun <reified T : View> viewAs(tag: Any = T::class.java.name): T {
    return view(tag) as T
  }
  
  fun imageView(tag: String) = viewAs<ImageView>(tag)
  
  fun textView(tag: String) = viewAs<TextView>(tag)
  
  fun editText(tag: String) = viewAs<EditText>(tag)
}