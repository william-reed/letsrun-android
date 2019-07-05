package dev.williamreed.letsrun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.williamreed.letsrun.R
import dev.williamreed.letsrun.ui.base.BaseViewModelFragment
import dev.williamreed.letsrun.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseViewModelFragment() {
    private val viewModel by lazyViewModel<HomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        refresh_layout.setOnRefreshListener { viewModel.fetchPostSummaries() }
        refresh_layout.isRefreshing = true

        val postSummaryAdapter = PostSummaryAdapter()

        post_summaries.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postSummaryAdapter
        }

        viewModel.getPostSummaries().observe(viewLifecycleOwner, Observer {
            postSummaryAdapter.updateData(it)
            refresh_layout.isRefreshing = false

        })
    }

}
