package dev.williamreed.letsrun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.williamreed.letsrun.R
import dev.williamreed.letsrun.ui.base.BaseViewModelFragment
import dev.williamreed.letsrun.viewmodel.ThreadViewModel
import kotlinx.android.synthetic.main.fragment_thread.*


class ThreadFragment : BaseViewModelFragment() {
    private val viewModel by lazyViewModel<ThreadViewModel>()
    private val args by navArgs<ThreadFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_thread, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setOnRefreshListener { viewModel.fetchReplies(args.threadId) }
        refresh_layout.isRefreshing = true

        viewModel.fetchReplies(args.threadId)

        // recycler view
        val adapter = RepliesAdapter()
        thread_replies.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(1)) {
                        viewModel.fetchNextPage(args.threadId)
                    }
                }
            })
        }

        // view model
        viewModel.getThreadReplies().observe(viewLifecycleOwner, Observer {
            refresh_layout.isRefreshing = false
            adapter.updateData(it)

        })
    }

}
