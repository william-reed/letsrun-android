package dev.williamreed.letsrun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.williamreed.letsrun.data.ThreadReply
import dev.williamreed.letsrun.service.ForumService
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

class ThreadViewModel @Inject constructor(val forumService: ForumService) : BaseViewModel() {
    private val threadReplies = MutableLiveData<List<ThreadReply>>()
    private val threadRepliesMutable = mutableListOf<ThreadReply>()

    private var pagesLoaded = 0
    // normally i would say this needs to be an atomic boolean but we are in main thread android land.
    private var fetchingNextPage = false

    /**
     * Fetch / refresh replies for the pages currently loaded
     */
    fun fetchReplies(threadId: Int) {
        threadRepliesMutable.clear()
        forumService.fetchThreadRepliesForPages(threadId, pagesLoaded + 1)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeNetwork({
                threadRepliesMutable.add(it)
                threadReplies.value = threadRepliesMutable
            }, {
                Timber.e(it)
                messageState.value = "Error getting thread replies."
            }, {
                pagesLoaded = 1
                Timber.e(threadRepliesMutable.size.toString())
            })
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

        // have there been any new items?
        var newItems = false

        forumService.fetchThreadReplies(threadId, pagesLoaded)
            .observeOn(AndroidSchedulers.mainThread())
            // always want this on error to occur, not just non-network error
            .doOnError { fetchingNextPage = false }
            .subscribeNetwork({
                newItems = true
                threadRepliesMutable.add(it)
                threadReplies.value = threadRepliesMutable
            }, {
                Timber.e(it)
                messageState.value = "Error getting more thread replies."
            }, {
                // _only_ increment pages loaded if we got something from that next page. this allows us to not care
                // if there isn't another page yet
                if (newItems) {
                    pagesLoaded++
                }
                fetchingNextPage = false
            })
    }

    fun getThreadReplies(): LiveData<List<ThreadReply>> = threadReplies

    companion object {
        const val REPLIES_PER_PAGE = 20
    }
}
