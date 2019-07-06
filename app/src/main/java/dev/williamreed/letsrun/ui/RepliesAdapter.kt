package dev.williamreed.letsrun.ui

import android.view.View
import dev.williamreed.letsrun.R
import dev.williamreed.letsrun.data.ThreadReply
import dev.williamreed.letsrun.ui.base.BaseAdapter
import dev.williamreed.letsrun.util.dateTimeToTimeAgo
import kotlinx.android.synthetic.main.item_thread_reply.view.*

class RepliesAdapter : BaseAdapter<ThreadReply>(R.layout.item_thread_reply) {
    override fun bindItem(item: ThreadReply, view: View) {
        view.timestamp.text = dateTimeToTimeAgo(item.datestamp.datestamp, view.context)
        view.author.text = item.author
        view.body_text.text = item.bodyText
    }
}
