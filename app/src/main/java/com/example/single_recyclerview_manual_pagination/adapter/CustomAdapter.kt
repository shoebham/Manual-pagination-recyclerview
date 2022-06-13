package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.models.BaseClass
import com.example.single_recyclerview_manual_pagination.models.Category
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.UiModel
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CustomAdapter<T>(var dataSet: BaseClass<T>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ: AsyncListDiffer<UiModel> = AsyncListDiffer(this, DiffCallBack())

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

        fun bind(sticker: UiModel.Item) {
            if (sticker.item == null) {
//                Glide.with(binding.root.context).load(R.drawable.placeholder)
//                    .into(binding.itemImageView)
                Glide.with(binding.root.context).load(R.drawable.placeholder)
                    .into(binding.itemImageView)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))

                binding.itemImageView.setColorFilter(color)
            } else {
                Glide.with(binding.root.context).load(sticker.item.fixedWidthFull?.png?.url)
                    .placeholder(R.drawable.placeholder).into(binding.itemImageView)
                binding.itemImageView.setColorFilter(null)

            }
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
            if (stickerHeader == null) {
                binding.textitem.text = "loading"
            } else {
                binding.textitem.text = stickerHeader
                binding.textitem.setOnClickListener {
                }
            }
        }
    }


    fun submitList(list: List<UiModel>) {
        differ.submitList(list)
    }

    private fun currentList(): List<UiModel> {
        return differ.currentList
    }

    private class DiffCallBack : DiffUtil.ItemCallback<UiModel>() {
        override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            val returnValue =
                (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.item?.id == newItem.item?.id) ||
                        (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.text == newItem.text)
            Log.i("diffutil", "areItemsTheSame() ${returnValue}");
            return returnValue
//            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
            val returnValue =
                (oldItem is UiModel.Item && newItem is UiModel.Item && oldItem.item?.id == newItem.item?.id) ||
                        (oldItem is UiModel.Header && newItem is UiModel.Header && oldItem.text == newItem.text)
            Log.i("diffutil", "areContentsTheSame() ${returnValue}");
            return returnValue

        }

        override fun getChangePayload(oldItem: UiModel, newItem: UiModel): Any? {
            Log.i("diffutil", "getChangePayload()");
            return super.getChangePayload(oldItem, newItem)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            -1 -> StickerHeaderViewHolder.from(parent)
            else -> StickerViewHolder.from(parent)
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
        } else if (item is UiModel.Item) {
            (holder as StickerViewHolder).bind(sticker = item)
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = currentList().size


    override fun getItemViewType(position: Int): Int {
        // Use peek over getItem to avoid triggering page fetch / drops, since
        // recycling views is not indicative of the user's current scroll position.
        return when (currentList().get(position)) {
            is UiModel.Header -> -1
            else -> 1
        }
    }


}
