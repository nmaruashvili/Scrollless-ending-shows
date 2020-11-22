package com.example.scrolllessendingshows.presentation.screens.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.scrolllessendingshows.R
import com.example.scrolllessendingshows.databinding.ItemErrorBinding
import com.example.scrolllessendingshows.databinding.ItemLoaderBinding
import com.example.scrolllessendingshows.databinding.ItemTvShowBinding
import com.example.scrolllessendingshows.domain.model.ShowData
import com.example.scrolllessendingshows.domain.model.ShowsPerPage

class TvShowListAdapter :
    ListAdapter<TvShowListAdapter.TvShowListItem, TvShowListAdapter.TvShowListItemViewHolder>(
        object : DiffUtil.ItemCallback<TvShowListItem>() {
            override fun areItemsTheSame(
                oldItem: TvShowListItem,
                newItem: TvShowListItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TvShowListItem, newItem: TvShowListItem) =
                oldItem == newItem
        }
    ) {

    private val tvShowListItems = ArrayList<TvShowListItem>()

    private var retryRequest: (() -> Unit)? = null

    private var listReplace: (() -> Unit)? = null

    private var onTvShowSelected: ((ShowData) -> Unit)? = null

    var currentPage = 1
        private set

    fun onRetryRequested(retryRequest: () -> Unit) {
        this.retryRequest = retryRequest
    }

    fun onListReplaced(listReplace: () -> Unit) {
        this.listReplace = listReplace
    }

    fun onTvShowSelected(onTvShowSelected: (ShowData) -> Unit) {
        this.onTvShowSelected = onTvShowSelected
    }

    fun loadShowsOnPage(
        showsPerPage: ShowsPerPage,
        clearPrevious: Boolean = false
    ) {
        removeLoaderOrError()
        val newList = mutableListOf<TvShowListItem>()
        if (clearPrevious) {
            tvShowListItems.clear()
        }
        tvShowListItems.addAll(showsPerPage.showDataList.map { TvShowListItem.ItemShowData(it) })
        newList.addAll(tvShowListItems)
        if (showsPerPage.nextPageAvailable) {
            currentPage = showsPerPage.currentPage
            newList.add(TvShowListItem.ItemLoader)
        } else {
            currentPage = -1
        }
        submitList(newList) {
            if (currentPage == 1) {
                listReplace?.invoke()
            }
        }
    }

    fun loadShowList(showList: List<ShowData>) {
        loadShowsOnPage(
            ShowsPerPage(showList, 1, false, 1)
        )
    }

    fun setError(errorMessage: String) {
        tvShowListItems.apply {
            if (last() is TvShowListItem.ItemLoader || last() is TvShowListItem.ItemError)
                removeLast()
            add(TvShowListItem.ItemError(errorMessage))
            submitList(ArrayList(this))
        }
    }

    fun setLoading() {
        removeLoaderOrError()
        tvShowListItems.add(TvShowListItem.ItemLoader)
        submitList(ArrayList(tvShowListItems))
    }

    //Great naming.. once again!
    private fun removeLoaderOrError() {
        tvShowListItems.apply {
            if (isNotEmpty() && (last() is TvShowListItem.ItemError || last() is TvShowListItem.ItemLoader)) {
                removeLast()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowListItemViewHolder {
        return when (viewType) {
            VIEW_TYPE_TV_SHOW_ITEM -> TvShowListItemViewHolder(
                ItemTvShowBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_LOADER -> TvShowListItemViewHolder(
                ItemLoaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_ERROR -> TvShowListItemViewHolder(
                ItemErrorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("incorrect viewType")
        }
    }

    override fun onBindViewHolder(holder: TvShowListItemViewHolder, position: Int) {
        when (val listItem = getItem(position)) {
            is TvShowListItem.ItemShowData -> holder.bindShowData(listItem.data)
            is TvShowListItem.ItemLoader -> holder.bindLoader()
            is TvShowListItem.ItemError -> holder.bindError(listItem.errorMessage)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is TvShowListItem.ItemShowData -> VIEW_TYPE_TV_SHOW_ITEM
            TvShowListItem.ItemLoader -> VIEW_TYPE_LOADER
            is TvShowListItem.ItemError -> VIEW_TYPE_ERROR
        }
    }

    inner class TvShowListItemViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindShowData(data: ShowData) {
            with(binding as ItemTvShowBinding) {
                titleText.text = data.originalName
                Glide.with(binding.root)
                    .load(data.posterUrl)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(posterImage)
                ratingText.text = data.voteAverage.toString()
                root.setOnClickListener {
                    onTvShowSelected?.invoke(data)
                }
            }
        }

        fun bindLoader() {}

        fun bindError(errorMessage: String) {
            with(binding as ItemErrorBinding) {
                errorText.text = errorMessage
                retryButton.setOnClickListener {
                    removeLoaderOrError()
                    tvShowListItems.add(TvShowListItem.ItemLoader)
                    submitList(ArrayList(tvShowListItems))
                    retryRequest?.invoke()
                }
            }
        }
    }

    sealed class TvShowListItem {
        data class ItemShowData(val data: ShowData) : TvShowListItem()
        object ItemLoader : TvShowListItem()
        data class ItemError(val errorMessage: String) : TvShowListItem()
    }

    companion object {
        const val VIEW_TYPE_TV_SHOW_ITEM = 0
        const val VIEW_TYPE_LOADER = 1
        const val VIEW_TYPE_ERROR = 2
    }
}