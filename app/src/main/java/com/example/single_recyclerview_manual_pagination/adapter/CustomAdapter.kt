package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CustomAdapter<T>(var dataSet: BaseClass<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var differ: AsyncListDiffer<UiModel<T>>
    companion object {
        private val HEADER = 0
        private val ITEM = 1
        private val bindCount = AtomicInteger(0)
    }

    class StickerViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): StickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemsBinding.inflate(layoutInflater, parent, false)
                return StickerViewHolder(binding)
            }
        }

        fun bind(sticker: UiModel.Item<Sticker>) {
            if (sticker.baseModelOfItem?.item == null) {
//                Glide.with(binding.root.context).load(R.drawable.placeholder)
//                    .into(binding.itemImageView)
                Glide.with(binding.root.context).load(R.drawable.placeholder)
                    .into(binding.itemImageView)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))

                binding.itemImageView.setColorFilter(color)
            } else {
                Glide.with(binding.root.context)
                    .load(sticker.baseModelOfItem.item?.fixedWidthFull?.png?.url)
                    .placeholder(R.drawable.placeholder).into(binding.itemImageView)
                binding.itemImageView.setColorFilter(null)

            }
        }

        fun bindBanner(item: UiModel.Banner<Sticker>) {
            Glide.with(binding.root.context)
                .load(item.url)
                .placeholder(R.drawable.placeholder).into(binding.itemImageView)
            binding.itemImageView.setColorFilter(null)
        }
    }

    class StickerHeaderViewHolder(private val binding: HeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): StickerHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return StickerHeaderViewHolder(binding)
            }
        }

        fun bind(stickerHeader: String?) {

            binding.textitem.text = stickerHeader


        }
    }

    class LoadMoreViewHolder(private val binding: LoadMoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): LoadMoreViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoadMoreBinding.inflate(layoutInflater, parent, false)
                return LoadMoreViewHolder(binding)
            }
        }

        fun bind(loadMore: UiModel.LoadMore<Sticker>) {
            binding.loadMore.isVisible = loadMore.visible
        }

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
            -1 -> CustomAdapter.StickerHeaderViewHolder.from(parent)
            0 -> LoadMoreViewHolder.from(parent)
            else -> CustomAdapter.StickerViewHolder.from(parent)
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
            (holder as StickerHeaderViewHolder).bind(stickerHeader = item.text)
        } else if (item is UiModel.Item<*>) {
            (holder as StickerViewHolder).bind(sticker = item as UiModel.Item<Sticker>)
        } else if (item is UiModel.Banner) {
            (holder as StickerViewHolder).bindBanner((item as UiModel.Banner<Sticker>))
        } else if (item is UiModel.LoadMore) {
            (holder as LoadMoreViewHolder).bind((item as UiModel.LoadMore<Sticker>))
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
