package dev.williamreed.letsrun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.williamreed.letsrun.data.PostSummary
import dev.williamreed.letsrun.service.ForumService
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Home View Model
 *
 * View model for the home fragment
 */
class HomeViewModel @Inject constructor(val forumService: ForumService) : BaseViewModel() {
    private val postSummaries = MutableLiveData<List<PostSummary>>()

    init {
        fetchPostSummaries()
    }

    fun fetchPostSummaries() {
        disposables.add(
            forumService.fetchPostSummaries()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeNetwork({
                    postSummaries.value = it
                }, {
                    Timber.e(it)
                    messageState.value = "Error occurred getting forum posts."
                })
        )
    }

    fun getPostSummaries(): LiveData<List<PostSummary>> = postSummaries
}
