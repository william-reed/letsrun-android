package dev.williamreed.letsrun.service

import dev.williamreed.letsrun.data.PostSummary
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

class ForumService {
    fun fetchPostSummaries(): Single<List<PostSummary>> =
        Single.fromCallable {
            val doc = Jsoup.connect("https://www.letsrun.com/forum").get()
            doc.select("div.threads_container div.thread_list_container ul.thread_list li.row")
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { threads ->
                // remove anything that is non-normal. a script tag is a good sign
                threads.filter { it.select("script").size == 0 }
            }
            .map { threads ->
                threads
                    // first three rows are garbage
                    .subList(3, threads.size)
                    .map { row ->
                        PostSummary(
                            row.select(".post_title").text(),
                            row.select(".post_author").text(),
                            row.select(".post_count").text().toInt(),
                            LocalDateTime.parse(row.select(".timestamp").text().toUpperCase(), formatter)
                        )
                    }
            }

    companion object {
        // 7/3/2019 3:08pm
        private val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mma").withZone(ZoneOffset.ofHours(-5))
    }
}
