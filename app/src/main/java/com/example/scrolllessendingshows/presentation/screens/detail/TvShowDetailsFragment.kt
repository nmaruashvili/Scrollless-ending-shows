package com.example.scrolllessendingshows.presentation.screens.detail

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.scrolllessendingshows.R
import com.example.scrolllessendingshows.databinding.FragmentTvShowDetailsBinding
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.presentation.model.Result
import com.example.scrolllessendingshows.presentation.screens.list.TvShowListAdapter
import com.example.scrolllessendingshows.presentation.utils.scrollToTop
import com.example.scrolllessendingshows.presentation.utils.setOnCollapseListener
import org.koin.android.ext.android.inject

class TvShowDetailsFragment : Fragment(R.layout.fragment_tv_show_details) {

    private var _binding: FragmentTvShowDetailsBinding? = null
    private val binding get() = _binding!!

    private var data: ShowData? = null
    private val tvShowId
        get() = data?.id!!

    private val viewModel: TvShowDetailsViewModel by inject()

    private val adapter: TvShowListAdapter by lazy {
        TvShowListAdapter().apply {
            onListReplaced {
                binding.recyclerView.scrollToTop()
            }
            onRetryRequested {
                viewModel.getSimilarTvShows(tvShowId)
            }
            onTvShowSelected {
                parentFragmentManager.beginTransaction()
                    .add(binding.container.id, newInstance(it))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        data = arguments?.getParcelable(ARG_SHOW_DATA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTvShowDetailsBinding.bind(view)

        binding.apply {
            data?.apply {
                viewModel.getSimilarTvShows(tvShowId)
                Glide.with(view)
                    .load(posterUrl)
                    .into(posterImage)

                Glide.with(view)
                    .load(coverUrl)
                    .into(coverImage)

                toolbarTitleText.text = originalName
                Glide.with(view)
                    .load(coverUrl)
                    .circleCrop()
                    .into(toolbarIcon)

                titleText.text = originalName
                ratingText.text = voteAverage.toString()
                releaseDateText.text = releaseDate
                overviewText.text = overview
            }
            appBar.setOnCollapseListener { collapsed ->
                if (collapsed) {
                    toolbarIcon.visibility = View.VISIBLE
                    toolbarTitleText.visibility = View.VISIBLE
                } else {
                    toolbarIcon.visibility = View.GONE
                    toolbarTitleText.visibility = View.GONE
                }
            }
            backButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
            recyclerView.adapter = adapter
            viewModel.similarTVShowListLiveData.observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Success -> {
                        val list = result.data
                        if (list.isEmpty()) {
                            viewSwitcher.showNext()
                        } else {
                            adapter.loadShowList(list)
                        }
                    }
                    is Result.Error -> adapter.setError(
                        result.exception.message ?: getString(R.string.unknown_error)
                    )
                    Result.Loading -> adapter.setLoading()
                }
            })
        }
    }

    companion object {
        private const val ARG_SHOW_DATA = "arg_show_data"

        @JvmStatic
        fun newInstance(data: ShowData) = TvShowDetailsFragment().apply {
            arguments = bundleOf(ARG_SHOW_DATA to data)
        }
    }
}