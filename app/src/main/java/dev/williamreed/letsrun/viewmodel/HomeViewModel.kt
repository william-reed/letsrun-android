package dev.williamreed.letsrun.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.williamreed.letsrun.data.PostSummary
import dev.williamreed.letsrun.service.ForumService
import io.reactivex.android.schedulers.AndroidSchedulers
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import javax.inject.Inject

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

    companion object {
        // 7/3/2019 3:08pm
        private val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mma").withZone(ZoneOffset.ofHours(-3))
    }
}
