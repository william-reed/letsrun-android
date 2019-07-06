package dev.williamreed.letsrun.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import dev.williamreed.letsrun.R
import dev.williamreed.letsrun.data.PostSummary
import dev.williamreed.letsrun.util.dateTimeToTimeAgo
import kotlinx.android.synthetic.main.item_post_summary.view.*

class PostSummaryAdapter(private val threadClicked: (Int) -> Unit) :
    RecyclerView.Adapter<PostSummaryAdapter.PostSummaryViewHolder>() {
    private val data = mutableListOf<PostSummary>()

    inner class PostSummaryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: PostSummary) {
            view.setOnClickListener { threadClicked(item.threadId) }
            view.title.text = item.title
            view.posts.text = item.posts.toString()
            view.last_updated.text = dateTimeToTimeAgo(item.lastUpdated, view.context)
            view.author.text = item.author
        }
    }

    /**
     * Set the data for this adapter
     */
    fun updateData(newData: List<PostSummary>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostSummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post_summary, parent, false)
        return PostSummaryViewHolder(view)
    }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: PostSummaryViewHolder, position: Int) {
        val color = if (position % 2 == 0) {
            R.color.forum_yellow
        } else {
            R.color.forum_gray
        }

        holder.itemView.setBackgroundColor(
            ResourcesCompat.getColor(
                holder.view.resources,
                color,
                holder.view.context.theme
            )
        )

        holder.bind(data[position])
    }
}
