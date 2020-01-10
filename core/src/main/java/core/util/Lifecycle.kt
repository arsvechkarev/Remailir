package core.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <T> LiveData<T>.observe(activity: AppCompatActivity, crossinline block: (T) -> Unit) {
  observe(activity, Observer { block(it) })
}

inline fun <T> LiveData<T>.observe(fragment: Fragment, crossinline block: (T) -> Unit) {
  observe(fragment, Observer { block(it) })
}

inline fun <reified T : ViewModel> Fragment.viewModelOf(
  factory: ViewModelProvider.Factory,
  block: T.() -> Unit = {}
): T {
  val viewModel = ViewModelProviders.of(this, factory)[T::class.java]
  viewModel.block()
  return viewModel
}

inline fun <reified T : ViewModel> FragmentActivity.viewModelOf(
  factory: ViewModelProvider.Factory,
  block: T.() -> Unit = {}
): T {
  val viewModel = ViewModelProviders.of(this, factory)[T::class.java]
  viewModel.block()
  return viewModel
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, block: (T) -> Unit) =
  liveData.observe(this, Observer(block))
