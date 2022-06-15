package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModel
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CustomAdapter<T>() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var differ: AsyncListDiffer<UiModel<T>>

    private lateinit var apiCallback: (item: UiModel<T>) -> Unit

//    fun apiListener(callback: (item: UiModel<T>) -> Unit){
//        apiCallback = callback
//    }

    interface ApiInterface {
        fun getItemsWithOffset(id: Int, offset: String, limit: Int)
    }

    lateinit var apiInterface: ApiInterface

    fun setApiListener(apiInter: ApiInterface) {
        apiInterface = apiInter
    }

    companion object {
        private val HEADER = 0
        private val ITEM = 1
        private val bindCount = AtomicInteger(0)
    }

    fun submitList(list: List<UiModel<T>>) {
        differ.submitList(list)
    }

    private fun currentList(): List<UiModel<T>> {
        return differ.currentList
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            -1 -> Viewholders.StickerHeaderViewHolder.from(parent)
            0 -> Viewholders.LoadMoreViewHolder.from(parent)
            else -> Viewholders.StickerViewHolder.from(parent)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val item = currentList()[position]
        Log.i(
            "shubham",
            "onbind count ${bindCount.incrementAndGet()} position$position item${item}"
        )
        if (item is UiModel.Header) {
            Log.i("shubham", "onbind count ${bindCount.get()} position$position item${item.text}")
            (holder as Viewholders.StickerHeaderViewHolder).bind(stickerHeader = item.text)
        } else if (item is UiModel.Item) {
            (holder as Viewholders.StickerViewHolder).bind(
                sticker = item as UiModel.Item<Sticker>,
                adapter = this as CustomAdapter<Sticker>,
                position
            )
        } else if (item is UiModel.Banner) {
            (holder as Viewholders.StickerViewHolder).bindBanner((item as UiModel.Banner<Sticker>))
        } else if (item is UiModel.LoadMore) {
            (holder as Viewholders.LoadMoreViewHolder).bind((item as UiModel.LoadMore<Sticker>))
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = currentList().size


    override fun getItemViewType(position: Int): Int {
        // Use peek over getItem to avoid triggering page fetch / drops, since
        // recycling views is not indicative of the user's current scroll position.
        return when (currentList().get(position)) {
            is UiModel.Header -> -1
            is UiModel.LoadMore -> 0
            else -> 1
        }
    }


}
