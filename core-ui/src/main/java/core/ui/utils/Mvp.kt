package core.ui.utils

import moxy.MvpDelegate
import moxy.MvpDelegateHolder
import moxy.MvpPresenter
import moxy.MvpView
import moxy.presenter.PresenterField
import kotlin.reflect.KProperty

/**
 * If view is in restore state, then commands from viewState should not be animated, just
 * switched to. This method helps determine that
 */
fun <T : MvpView> T.shouldAnimate(presenter: MvpPresenter<T>): Boolean {
  return !presenter.isInRestoreState(this)
}

inline fun <reified T : MvpPresenter<*>> MvpDelegateHolder.moxyPresenter(
  name: String = "presenter",
  noinline factory: () -> T
): MoxyKtxDelegate<T> {
  return MoxyKtxDelegate(mvpDelegate, T::class.java.name + "." + name, factory)
}

class MoxyKtxDelegate<T : MvpPresenter<*>>(
  delegate: MvpDelegate<*>,
  name: String,
  private val factory: () -> T
) {
  
  private var presenter: T? = null
  
  init {
    val field = object : PresenterField<Any?>(name, null, null) {
      override fun providePresenter(delegated: Any?): MvpPresenter<*> = factory()
      override fun bind(container: Any?, presenter: MvpPresenter<*>) {
        @Suppress("UNCHECKED_CAST")
        this@MoxyKtxDelegate.presenter = presenter as T
      }
    }
    delegate.registerExternalPresenterField(field)
  }
  
  operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return presenter ?: throw IllegalStateException(
      "Presenter can be accessed only after MvpDelegate.onCreate() call"
    )
  }
}