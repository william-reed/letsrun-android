package dev.williamreed.letsrun.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Base Adapter
 *
 * Generic adapter pattern.
 */
abstract class BaseAdapter<T>(@LayoutRes private val itemLayout: Int) :
    RecyclerView.Adapter<BaseAdapter<T>.BaseAdapterViewHolder>() {
    private val data = mutableListOf<T>()

    inner class BaseAdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(item: T) {
            bindItem(item, view)
        }
    }

    /**
     * Set the data for this adapter
     */
    fun updateData(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(itemLayout, parent, false)
        return BaseAdapterViewHolder(view)
    }

    override fun getItemCount() = data.size
    override fun onBindViewHolder(holder: BaseAdapterViewHolder, position: Int) =
        holder.bind(data[position])

    /**
     * Bind the item [T] to the view
     */
    abstract fun bindItem(item: T, view: View)
}
