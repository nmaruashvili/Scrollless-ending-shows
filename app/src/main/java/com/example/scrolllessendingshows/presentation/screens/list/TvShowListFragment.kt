package com.example.scrolllessendingshows.presentation.screens.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.scrolllessendingshows.R
import com.example.scrolllessendingshows.databinding.FragmentTvShowListBinding
import com.example.scrolllessendingshows.presentation.model.RequestedResult
import com.example.scrolllessendingshows.presentation.screens.detail.TvShowDetailsFragment
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_INIT
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_LOAD_MORE
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_SEARCH
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListViewModel.Companion.RC_SWIPE_REFRESH
import com.example.scrolllessendingshows.presentation.utils.LinearEndlessScrollListener
import com.example.scrolllessendingshows.presentation.utils.closeKeyboard
import com.example.scrolllessendingshows.presentation.utils.scrollToTop
import org.koin.android.ext.android.inject

class TvShowListFragment : Fragment(R.layout.fragment_tv_show_list) {

    private var _binding: FragmentTvShowListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TvShowListViewModel by inject()

    private val adapter: TvShowListAdapter by lazy {
        TvShowListAdapter().apply {
            onListReplaced {
                binding.recyclerView.scrollToTop()
            }
            onRetryRequested {
                viewModel.getShows(currentPage + 1, true)
            }
            onTvShowSelected {
                binding.searchView.clearFocus()
                closeKeyboard(requireContext(), binding.searchView)
                parentFragmentManager.beginTransaction()
                    .add(binding.container.id, TvShowDetailsFragment.newInstance(it))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvShowListBinding.bind(view)
        viewModel.onInit()
        binding.apply {
            recyclerView.adapter = adapter
            recyclerView.addOnScrollListener(object : LinearEndlessScrollListener() {
                override fun requestNextPage() {
                    viewModel.getShows(adapter.currentPage + 1)
                }
            })
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.searchShows(query) }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { viewModel.searchShows(newText) }
                    return false
                }

            })
            swipeRefreshLayout.setOnRefreshListener {
                viewModel.onRefresh()
            }
            viewModel.showsPerPageLiveData.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is RequestedResult.Success -> {
                        when (result.requestCode) {
                            RC_INIT,
                            RC_SEARCH,
                            RC_SWIPE_REFRESH -> adapter.loadShowsOnPage(result.data, true)
                            RC_LOAD_MORE -> adapter.loadShowsOnPage(result.data, false)
                        }
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is RequestedResult.Error -> {
                        showError(
                            result.requestCode,
                            result.exception.message ?: getString(R.string.unknown_error)
                        )
                        swipeRefreshLayout.isRefreshing = false
                    }
                    is RequestedResult.Loading -> {
                        swipeRefreshLayout.isRefreshing = result.requestCode != RC_LOAD_MORE
                    }
                }
            })
        }
    }

    private fun showError(requestCode: Int, message: String) {
        when (requestCode) {
            RC_INIT,
            RC_SEARCH,
            RC_SWIPE_REFRESH -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            RC_LOAD_MORE -> adapter.setError(message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = TvShowListFragment()
    }
}