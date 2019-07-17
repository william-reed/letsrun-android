package dev.williamreed.letsrun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.williamreed.letsrun.data.ThreadReply
import dev.williamreed.letsrun.service.ForumService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Thread View Model
 *
 * VM for the thread fragment
 */
class ThreadViewModel @Inject constructor(val forumService: ForumService) : BaseViewModel() {
    private val threadReplies = MutableLiveData<List<ThreadReply>>()
    private val threadRepliesMutable = TreeSet<ThreadReply>(kotlin.Comparator { o1, o2 -> (o1.id - o2.id).toInt() })

    private var pagesLoaded = 0
    // normally i would say this needs to be an atomic boolean but we are in main thread android land.
    private var fetchingNextPage = false
    private var fetchNextDisposable: Disposable? = null

    /**
     * Fetch / refresh replies for the pages currently loaded
     */
    fun fetchReplies(threadId: Int) {
        // if anything is being fetched, cancel it
        fetchNextDisposable?.dispose()

        threadRepliesMutable.clear()
        fetchingNextPage = true

        disposables.add(
            forumService.fetchThreadRepliesForPages(threadId, pagesLoaded + 1)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { fetchingNextPage = false }
                .subscribeNetwork({
                    threadRepliesMutable.addAll(it)
                    threadReplies.value = threadRepliesMutable.toList()
                }, {
                    Timber.e(it)
                    messageState.value = "Error getting thread replies."
                }, {
                    fetchingNextPage = false
                    pagesLoaded = 1
                })
        )
    }

    /**
     * Fetch the next page. Implements a lock-like behavior so your can't call this again until the previous calling
     * completes
     */
    fun fetchNextPage(threadId: Int) {
        // there definitely isn't another page if we haven't maxed out this page
        if (fetchingNextPage || threadRepliesMutable.size < pagesLoaded * REPLIES_PER_PAGE) return

        // this is my "lock"
        fetchingNextPage = true

        fetchNextDisposable =
            forumService.fetchThreadReplies(threadId, pagesLoaded)
                .observeOn(AndroidSchedulers.mainThread())
                // always want this on error to occur, not just non-network error
                .doOnError { fetchingNextPage = false }
                .subscribeNetwork({
                    // _only_ increment pages loaded if we got something from that next page. this allows us to not care
                    // if there isn't another page yet
                    if (it.isNotEmpty()) {
                        pagesLoaded++
                    }
                    threadRepliesMutable.addAll(it)
                    threadReplies.value = threadRepliesMutable.toList()
                    fetchingNextPage = false
                }, {
                    Timber.e(it)
                    messageState.value = "Error getting more thread replies."
                }).also { disposables.add(it) }
    }

    fun getThreadReplies(): LiveData<List<ThreadReply>> = threadReplies

    companion object {
        const val REPLIES_PER_PAGE = 20
    }
}
