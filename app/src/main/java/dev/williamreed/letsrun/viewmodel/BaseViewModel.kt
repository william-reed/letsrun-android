package dev.williamreed.letsrun.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.williamreed.letsrun.util.KotlinUtils
import io.reactivex.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Base View Model
 *
 * A view model that has arbitrary state.
 *
 * ### Features
 * - CompositeDisposable to manage the ReactiveX lifecycle for you
 * - message state which works great in junction with [com.busright.watchdog.ui.BaseViewModelFragment] to automatically
 * display the messages
 * - `subscribeNetwork` which automatically handles error messages if the subscribed [Single], [Observable],
 * [Flowable], [Completable], or [Maybe] emits a network error by setting the message state to show that.
 */
abstract class BaseViewModel : ViewModel() {
    protected val disposables = CompositeDisposable()
    protected val messageState = SingleLiveEvent<String>()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    /**
     * If a network exception occurs set the message state automatically, otherwise send it to the provided [onError]
     */
    fun <T> Single<T>.subscribeNetwork(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit): Disposable =
        subscribe(onSuccess, { networkError(it, onError) })

    /**
     * If a network exception occurs set the message state, otherwise send it to [onError]
     */
    fun <T> Observable<T>.subscribeNetwork(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit = KotlinUtils.emptyFunction
    ): Disposable = subscribe(onNext, { networkError(it, onError) }, onComplete)

    /**
     * If a network exception occurs set the message state, otherwise send it to [onError]
     */
    fun <T> Flowable<T>.subscribeNetwork(
        onNext: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit = KotlinUtils.emptyFunction
    ): Disposable = subscribe(onNext, { networkError(it, onError) }, onComplete)

    /**
     * If a network exception occurs set the message state, otherwise send it to [onError]
     */
    fun <T> Maybe<T>.subscribeNetwork(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onComplete: () -> Unit = KotlinUtils.emptyFunction
    ): Disposable = subscribe(onSuccess, { networkError(it, onError) }, onComplete)

    /**
     * If a network exception occurs set the message state, otherwise send it to [onError]
     */
    fun Completable.subscribeNetwork(onComplete: () -> Unit, onError: (Throwable) -> Unit): Disposable =
        subscribe(onComplete, { networkError(it, onError) })

    /**
     * Swallow the exception if it is network related otherwise pass it on to [nonNetworkError]
     */
    private fun networkError(throwable: Throwable, nonNetworkError: (Throwable) -> Unit) {
        when (throwable) {
            // not catching an IOException here, but maybe I should.
            // don't want to be overly broad if I don't have to be
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                // show network error message

                messageState.value = "Network error occurred."
            }
            else -> nonNetworkError(throwable)
        }
    }

    /**
     * Get the message state to figure out which message should be shown currently. Automatically handled with the
     * [com.busright.watchdog.ui.BaseViewModelFragment]
     */
    fun getMessageState(): LiveData<String> = messageState
}
