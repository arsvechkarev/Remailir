package core.di

import android.app.Activity
import androidx.fragment.app.Fragment

interface CoreComponentProvider {
  
  val coreComponent: CoreComponent
}

val Activity.coreComponent get() = (application as CoreComponentProvider).coreComponent
val Fragment.coreComponent get() = (requireActivity().application as CoreComponentProvider).coreComponent