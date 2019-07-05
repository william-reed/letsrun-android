package dev.williamreed.letsrun.data

data class ThreadReply(
    val id: Int,
    val thread: Int,
    val author: String,
    val parent: Int,
    val subject: String,
    val bodyText: String
)
