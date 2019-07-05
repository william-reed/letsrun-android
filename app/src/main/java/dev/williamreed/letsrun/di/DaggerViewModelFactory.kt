package dev.williamreed.letsrun.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * ViewModel Factory
 *
 * Major help from tutorial
 * https://blog.kotlin-academy.com/understanding-dagger-2-multibindings-viewmodel-8418eb372848
 */
@Singleton
class DaggerViewModelFactory
@Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // check if we have an entry for it in the map
        val creator = creators[modelClass]
        // maybe we have a subclass instead
            ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
            // looks like we don't know how to create this class so throw an exception
            ?: throw IllegalArgumentException("Unknown ViewModel class $modelClass")

        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
