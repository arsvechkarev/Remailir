package core.di.viewmodel

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.reflect.KClass

@MapKey
@Target(FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
annotation class ViewModelKey(val value: KClass<out ViewModel>)