package dev.williamreed.letsrun.ui.base

import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.AndroidSupportInjection
import dev.williamreed.letsrun.viewmodel.BaseViewModel
import javax.inject.Inject

/**
 * Base View Model Fragment
 *
 * Automatically obtains a view model from the given type and automatically injected in [onAttach]
 */
abstract class BaseViewModelFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    /**
     * Lazily get the view model
     *
     * @param showMessage show messages from the BaseViewModel
     */
    inline fun <reified VM : BaseViewModel> lazyViewModel(showMessage: Boolean = true) = lazy {
        val vm = ViewModelProviders.of(this, viewModelFactory).get(VM::class.java)
        if (showMessage) vm.getMessageState().observe(this, Observer { showMessage(it) })
        vm
    }
}
