package dev.williamreed.letsrun.service

import dev.williamreed.letsrun.data.PostSummary
import dev.williamreed.letsrun.data.ThreadReply
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter

/**
 * Forum Service
 *
 * The interface to LetsRun data
 */
class ForumService {

    /**
     * Fetch the home page post summaries
     */
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
                            row.select(".post_title > a").attr("href").split("=")[1].toInt(),
                            row.select(".post_title").text(),
                            row.select(".post_author").text(),
                            row.select(".post_count").text().toInt(),
                            LocalDateTime.parse(row.select(".timestamp").text().toUpperCase(), formatter)
                        )
                    }
            }

    /**
     * Fetch the thread replies
     * @param threadId the thread id
     * @param page the page of the post, starting at 0 (as is done in the letsrun URL)
     */
    fun fetchThreadReplies(threadId: Int, page: Int = 0): Single<List<ThreadReply>> =
        // get all of the reply id's first
        Single.fromCallable {
            Jsoup.connect("https://www.letsrun.com/forum/flat_read.php?thread=$threadId&page=$page").get()
                .select("div.page_content div.thread_list_container ul.thread > a")
        }
            .subscribeOn(Schedulers.io())
            // TODO: after this step we want to parallelize these
            .observeOn(Schedulers.computation())
            .toFlowable()
            .flatMapIterable { it }
            // the actual reply id
            .map { it.attr("name") }
            .observeOn(Schedulers.io())
            // get the thread where the reply data is
            .map { Jsoup.connect("https://www.letsrun.com/forum/posts/$it/reply").get() }
            .observeOn(Schedulers.computation())
            // find the full reply data
            .map {
                // 0th value is always the whole match, 1st value is capture group
                // this escape is not redundant on android. its a bug saying it is. its redundant in pure kotlin though.
                Regex(
                    "window\\.App\\.state\\.reply = (\\{.*?\\});\\s*\$",
                    RegexOption.MULTILINE
                ).find(it.toString())?.groups?.get(1)?.value
                    ?: error("Reply not found in post")
            }
            // parse the JSON
            .map { Json.nonstrict.parse(ThreadReply.serializer(), it) }
            .toList()

    /**
     * Fetch the thread replies for multiple pages
     * @param threadId the thread id
     * @param pages the pages to fetch, inclusive
     */
    fun fetchThreadRepliesForPages(threadId: Int, pages: Int): Flowable<List<ThreadReply>> =
        Flowable.range(0, pages)
            .flatMapSingle { fetchThreadReplies(threadId, it) }

    private companion object {
        // 7/3/2019 3:08pm
        // it doesn't seem like the .ofHours does anything. Need to figure out if LetsRun timestamp is always -4 or not.
        private val formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mma").withZone(ZoneOffset.ofHours(-5))
    }
}
