package com.medetzhakupov.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @return a new instance of [FragmentFactory] which will delegate each Fragment instantiation to the provided [makeFragment] factory function.
 * In case the provided factory function returns null - the Fragment will be attempted to be created using the default no-args constructor
 */
inline fun fragmentFactory(crossinline makeFragment: (Class<out Fragment>) -> Fragment?) = object : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val fragmentClass = loadFragmentClass(classLoader, className)

        return makeFragment(fragmentClass)
            ?: super.instantiate(classLoader, className)
    }
}

/**
 * Attempts to create a Fragment of type [T] and throws an Exception if unsuccessful
 */
inline fun <reified T : Fragment> FragmentActivity.createFragment(): T = supportFragmentManager.fragmentFactory.instantiate(
    checkNotNull(T::class.java.classLoader),
    T::class.java.name
) as? T ?: throw IllegalArgumentException("This Fragment factory cannot create Fragments of type ${T::class.java.name}")


/**
 * Returns the requested ViewModel instance, creating it first, using the provided 'createViewModel' factory function,
 * if the ViewModel hasn't been initialised yet
 */
inline fun <reified T : ViewModel> Fragment.viewModel(
    crossinline createViewModel: (handle: SavedStateHandle) -> T
) = lazy {
    ViewModelProvider(
        this,
        object : AbstractSavedStateViewModelFactory(this, null) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(key: String, modelClass: Class<T>, handle: SavedStateHandle) = createViewModel(handle) as T
        }
    ).get(T::class.java)
}
