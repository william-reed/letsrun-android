package dev.williamreed.letsrun.data

import org.threeten.bp.LocalDateTime

data class PostSummary(val threadId: Int, val title: String, val author: String, val posts: Int, val lastUpdated: LocalDateTime)
