package dev.williamreed.letsrun.data

import org.threeten.bp.LocalDateTime

/**
 * Post Summary as seen on the forum home page
 */
data class PostSummary(val threadId: Int, val title: String, val author: String, val posts: Int, val lastUpdated: LocalDateTime)
