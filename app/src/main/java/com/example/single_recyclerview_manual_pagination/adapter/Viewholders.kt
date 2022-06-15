package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.State
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.UiModel
import java.util.*

class Viewholders {

    class StickerViewHolder(private val binding: ItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): StickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemsBinding.inflate(layoutInflater, parent, false)
                return StickerViewHolder(binding)
            }
        }

        fun bind(sticker: UiModel.Item<Sticker>, adapter: CustomAdapter<Sticker>, position: Int) {
            if (sticker.baseModelOfItem.item == null || sticker.baseModelOfItem.state == State.NOT_LOADING || sticker.baseModelOfItem.state == State.LOADING) {
//                Glide.with(binding.root.context).load(R.drawable.placeholder)
//                    .into(binding.itemImageView)
                Glide.with(binding.root.context).load(R.drawable.placeholder)
                    .into(binding.itemImageView)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
                binding.itemImageView.setColorFilter(color)
//                sticker.baseModelOfItem.state = State.LOADING

                if (sticker.baseModelOfItem.state == State.NOT_LOADING) {
                    if (sticker.baseModelOfItem.category != null && sticker.baseModelOfItem.category?.id != null) {
                        adapter.apiInterface.getItemsWithOffset(
                            sticker.baseModelOfItem.category?.id!!,
                            (sticker.baseModelOfItem.categoryBasedPosition!! + 1).toString(),
                            sticker.baseModelOfItem.category?.initialCount!!
                        )
                        var i = position
                        while (adapter.differ.currentList[i] is UiModel.Item) {
                            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
                                State.LOADING
                            i++
                        }
//                        for (i in position until position + sticker.baseModelOfItem.category?.initialCount!!) {
//                            if (adapter.differ.currentList[i] is UiModel.Item) {
//                                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
//                                    State.LOADING
//                            }
//                        }
//                        sticker.baseModelOfItem.state = State.LOADING
                    }
                }
            } else if (sticker.baseModelOfItem.state == State.LOADED) {
                Glide.with(binding.root.context)
                    .load(sticker.baseModelOfItem.item.fixedWidthFull?.png?.url)
                    .placeholder(R.drawable.placeholder).into(binding.itemImageView)
                binding.itemImageView.setColorFilter(null)
//                sticker.baseModelOfItem.state = State.LOADED
            }
        }

        fun callBindOnVisibleScreen() {

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
            binding.loadMore.setOnClickListener {

            }
        }
    }
}