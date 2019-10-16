package com.arsvechkarev.core.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

inline fun <reified T : ViewModel> Fragment.viewModel(
  factory: ViewModelProvider.Factory,
  block: T.() -> Unit
): T {
  val viewModel = ViewModelProviders.of(this, factory)[T::class.java]
  viewModel.block()
  return viewModel
}

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(liveData: L, block: (T) -> Unit) =
  liveData.observe(this, Observer(block))
