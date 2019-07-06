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

    fun fetchReplies(threadId: Int) {
        threadRepliesMutable.clear()
        forumService.fetchThreadReplies(threadId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeNetwork({
                threadRepliesMutable.add(it)
                threadReplies.value = threadRepliesMutable
            }, {
                Timber.e(it)
                messageState.value = "Error getting thread replies."
            })
    }

    fun getThreadReplies(): LiveData<List<ThreadReply>> = threadReplies
}
