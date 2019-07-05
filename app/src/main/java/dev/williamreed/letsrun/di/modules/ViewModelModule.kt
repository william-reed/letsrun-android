package dev.williamreed.letsrun.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import dev.williamreed.letsrun.di.DaggerViewModelFactory
import dev.williamreed.letsrun.viewmodel.HomeViewModel
import kotlin.reflect.KClass

/**
 * Annotation definition for manually creating ViewModel providers / bindings
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @[Binds IntoMap ViewModelKey(HomeViewModel::class)]
    internal abstract fun bindHomeViewModel(viewModel: HomeViewModel): ViewModel
}
