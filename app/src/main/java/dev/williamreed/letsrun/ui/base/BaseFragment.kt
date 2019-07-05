package dev.williamreed.letsrun.ui.base

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

abstract class BaseFragment : Fragment() {

    // use a reference to the lazy object so we can only destroy it if it has been initialized
    private val lazyDisposables = lazy { CompositeDisposable() }
    /**
     * Any fragment related disposables. Automatically disposed in [onDestroyView]
     */
    val disposables: CompositeDisposable by lazyDisposables

    /**
     * Show the given message. Currently done  in a snackbar which is prone to change
     * @param message the string to show
     */
    fun showMessage(message: String) {
        if (this.view == null) Timber.w("showMessage called before view attached to fragment.")

        Snackbar.make(
            this.view ?: return,
            message, Snackbar.LENGTH_INDEFINITE
        ).apply {
            // dismiss on click
            setAction("Dismiss") { this.dismiss() }

            show()
        }
    }

    /**
     * Hide the keyboard
     */
    fun hideKeyboard() {
        val imm = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()

        // not going to trigger an initialization unless it has been created
        if (lazyDisposables.isInitialized()) {
            disposables.clear()
        }
    }
}
