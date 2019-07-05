package dev.williamreed.letsrun.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.williamreed.letsrun.R
import dev.williamreed.letsrun.ui.base.BaseViewModelFragment
import dev.williamreed.letsrun.viewmodel.ThreadViewModel

class ThreadFragment : BaseViewModelFragment() {
    private val viewModel by lazyViewModel<ThreadViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_thread, container, false)
    }


}
