package com.example.single_recyclerview_manual_pagination

import android.content.ClipData
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.models.BaseClass
import com.example.single_recyclerview_manual_pagination.models.Category
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerUiModel
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class CustomAdapter(var dataSet: BaseClass) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val differ: AsyncListDiffer<Category> = AsyncListDiffer(this, DiffCallBack())

    companion object {
        private val HEADER = 0
        private val ITEM = 1
        private val bindCount = AtomicInteger(0)
    }

    private class DiffCallBack : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.initialCount == newItem.initialCount
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.itemList == newItem.itemList
        }

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

        fun bind(sticker: Sticker?) {
            if (sticker == null) {
//                Glide.with(binding.root.context).load(R.drawable.placeholder)
//                    .into(binding.itemImageView)
                Glide.with(binding.root.context).load(R.drawable.placeholder)
                    .into(binding.itemImageView)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))

                binding.itemImageView.setColorFilter(color)
            } else {
                Glide.with(binding.root.context).load(sticker.fixedWidthFull?.png?.url)
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

    fun submitList(list: List<Category>) {
        differ.submitList(list)
    }

    fun currentList(): List<Category> {
        return differ.currentList
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

            (holder as StickerViewHolder).bind(sticker = dataSet.getCategory().itemList[item])
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
