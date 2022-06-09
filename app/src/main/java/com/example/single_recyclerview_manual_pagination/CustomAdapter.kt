package com.example.single_recyclerview_manual_pagination

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.models.BaseClass
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerUiModel
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CustomAdapter(private val dataSet: BaseClass) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

        fun bind(sticker: Sticker) {
            Glide.with(binding.root.context).load(sticker.fixedWidthFull?.png?.url)
                .placeholder(R.drawable.placeholder).into(binding.itemImageView)
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

        fun bind(stickerHeader: String) {
            binding.textitem.text = stickerHeader
            binding.textitem.setOnClickListener {
            }
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
        val item = getItemViewType(position)
        Log.i("shubham", "onbind count ${bindCount.incrementAndGet()} position$position item$item")
        if (item == -1) {
            (holder as StickerHeaderViewHolder).bind(stickerHeader = dataSet.getCategory().name)
        } else {
            (holder as StickerViewHolder).bind(sticker = dataSet.getCategory().itemList.get(item))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.getSize()

    override fun getItemViewType(position: Int): Int {
        // Use peek over getItem to avoid triggering page fetch / drops, since
        // recycling views is not indicative of the user's current scroll position.
        val item = dataSet.getAt(position)
        if (item < 0)
            return -1
        else return item

    }


}
