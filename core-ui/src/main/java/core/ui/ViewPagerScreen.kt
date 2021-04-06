package core.ui

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpView
import viewdsl.childView

abstract class ViewPagerScreen : MvpDelegateHolder, MvpView {
  
  private var isParentDelegateSet = false
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
  
  internal fun createLayout(context: Context) {
    _view = buildView(context)
  }
  
  internal fun onCreateDelegate(parent: MvpDelegate<*>) {
    if (!isParentDelegateSet) {
      mvpDelegate.setParentDelegate(parent, this::class.java.name)
      isParentDelegateSet = true
    }
    mvpDelegate.onCreate()
  }
  
  internal fun onAttachDelegate() {
    mvpDelegate.onAttach()
  }
  
  internal fun onDetachDelegate() {
    if (isParentDelegateSet) {
      mvpDelegate.onDetach()
    }
  }
  
  internal fun onDestroyDelegate() {
    mvpDelegate.onDestroyView()
    if (isParentDelegateSet) {
      mvpDelegate.onDestroy()
    }
  }
  
  abstract fun buildView(context: Context): View
  
  open fun onBackPressed(): Boolean = false
  
  open fun onInit() = Unit
  
  open fun onRelease() = Unit
  
  fun getText(@StringRes resId: Int, vararg args: Any): CharSequence {
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