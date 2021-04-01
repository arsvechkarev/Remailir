package com.arsvechkarev.core.navigation

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.arsvechkarev.viewdsl.ViewBuilder
import com.arsvechkarev.viewdsl.childView
import com.arsvechkarev.viewdsl.withViewBuilder
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpView

@Suppress("PropertyName", "MemberVisibilityCanBePrivate")
abstract class Screen : MvpDelegateHolder, MvpView {
  
  @PublishedApi
  internal val viewsCache = HashMap<Any, View>()
  
  private var _mvpDelegate: MvpDelegate<out Screen>? = null
  
  internal val metadata = ScreenMetadata()
  
  val arguments get() = metadata._arguments
  
  val view get() = metadata._view
  
  val context get() = metadata._context
  
  val viewNonNull: View get() = view!!
  
  val contextNonNull get() = context!!
  
  val activityNonNull get() = context as AppCompatActivity
  
  val navigator get() = context as Navigator
  
  abstract fun buildLayout(): View
  
  override fun getMvpDelegate(): MvpDelegate<*> {
    return _mvpDelegate ?: run {
      _mvpDelegate = MvpDelegate(this)
      _mvpDelegate!!
    }
  }
  
  internal fun onInitDelegate() {
    mvpDelegate.onCreate()
  }
  
  internal fun onAppearedOnScreenDelegate() {
    mvpDelegate.onAttach()
  }
  
  internal fun onDetachDelegate() {
    mvpDelegate.onSaveInstanceState()
    mvpDelegate.onDetach()
  }
  
  internal fun onDestroyDelegate() {
    mvpDelegate.onDestroyView()
    mvpDelegate.onDestroy()
  }
  
  open fun onBackPressed(): Boolean = false
  
  open fun onInit() = Unit
  
  open fun onInit(arguments: Bundle) = Unit
  
  open fun onAppearedOnScreen() = Unit
  
  open fun onNetworkAvailable() = Unit
  
  open fun onOrientationBecamePortrait() = Unit
  
  open fun onOrientationBecameLandscape() = Unit
  
  open fun onRelease() = Unit
  
  fun hideKeyboard() {
    val imm = contextNonNull.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(viewNonNull.windowToken, 0)
  }
  
  fun withViewBuilder(builder: ViewBuilder.() -> View): View {
    return contextNonNull.withViewBuilder(builder)
  }
  
  fun getString(@StringRes resId: Int, vararg args: Any): CharSequence {
    return contextNonNull.getString(resId, *args)
  }
  
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
  
  fun imageView(tag: Any) = viewAs<ImageView>(tag)
  
  fun textView(tag: Any) = viewAs<TextView>(tag)
  
  fun editText(tag: Any) = viewAs<EditText>(tag)
}