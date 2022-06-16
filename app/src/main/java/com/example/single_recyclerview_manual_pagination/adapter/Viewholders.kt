package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItem
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
            if (sticker.baseModelOfItem.item == null) {
//                Glide.with(binding.root.context).load(R.drawable.placeholder)
//                    .into(binding.itemImageView)
                Glide.with(binding.root.context).load(R.drawable.placeholder)
                    .into(binding.itemImageView)
                val rnd = Random()
                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
                binding.itemImageView.setColorFilter(color)
//                sticker.baseModelOfItem.state = State.LOADING

                if (sticker.baseModelOfItem.state == State.NOT_LOADING && !sticker.baseModelOfItem.isLoadMoreClicked) {
                    if (sticker.baseModelOfItem.category != null && sticker.baseModelOfItem.category?.id != null) {
                        callBindOnVisibleScreen(
                            adapter, position,
                            id = sticker.baseModelOfItem.category?.id!!,
                            offset = (sticker.baseModelOfItem.categoryBasedPosition?.plus(1)).toString(),
                            limit = sticker.baseModelOfItem.category?.initialCount!!
                        )
//                        for (i in position until position + sticker.baseModelOfItem.category?.initialCount!!) {
//                            if (adapter.differ.currentList[i] is UiModel.Item) {
//                                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
//                                    State.LOADING
//                            }
//                        }
                        sticker.baseModelOfItem.state = State.LOADING
                    }
                } else if (sticker.baseModelOfItem.isLoadMoreClicked) {


                }
            } else if (sticker.baseModelOfItem.state == State.LOADED) {
                Glide.with(binding.root.context)
                    .load(sticker.baseModelOfItem.item?.fixedWidthFull?.png?.url)
                    .placeholder(R.drawable.placeholder).into(binding.itemImageView)
                binding.itemImageView.setColorFilter(null)
//                sticker.baseModelOfItem.state = State.LOADED
            }
        }

        fun callBindOnVisibleScreen(
            adapter: CustomAdapter<Sticker>,
            position: Int,
            id: Int,
            offset: String,
            limit: Int
        ) {
            adapter.apiInterface.getItemsWithOffset(
                id,
                offset,
                limit
            )
            var i = position
            while (adapter.differ.currentList[i] is UiModel.Item) {
                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
                    State.LOADING
                i++
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

        fun callBindOnVisibleScreen(
            adapter: CustomAdapter<Sticker>,
            position: Int,
            id: Int,
            offset: String,
            limit: Int
        ) {
            adapter.apiInterface.getItemsWithOffset(
                id,
                offset,
                limit
            )
            var i = position
            while (adapter.differ.currentList[i] is UiModel.Item) {
                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
                    State.LOADING
                i++
            }
        }

        fun bind(
            loadMore: UiModel.LoadMore<Sticker>,
            adapter: CustomAdapter<Sticker>,
            position: Int
        ) {
            if (loadMore.itemAbove != null) {
                loadMore.visible = !loadMore.itemAbove.isLastItem
            }
            binding.loadMore.isVisible = loadMore.visible
            binding.loadMore.setOnClickListener {

                val category = adapter.dataset.listOfItems.find { it.id == loadMore.id }
                loadMore.itemAbove?.isLoadMoreClicked = true
                if (category != null) {
                    val remaining = category.total
                    var tempList = mutableListOf<BaseModelOfItem<Sticker>>()
                    tempList = category.itemList.toMutableList()
                    repeat(remaining) {
                        tempList.add(BaseModelOfItem(isLoadMoreClicked = true))
                    }
                    category.itemList = tempList
                    val uiModellist =
                        adapter.convertToUiModel(adapter.dataset, adapter.dataset.listOfItems)
                    var i = position
                    adapter.submitList(uiModellist)
                    callBindOnVisibleScreen(
                        adapter, position,
                        id = category.id!!,
                        offset = (loadMore.itemAbove?.categoryBasedPosition?.plus(1)).toString(),
                        limit = remaining
                    )
                    while (adapter.differ.currentList[i] is UiModel.Item) {
                        (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.isLoadMoreClicked =
                            true
                        (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
                            State.LOADING
                        i++
                    }
                    Log.i("baseclass", "${adapter.dataset}")
//                    loadMore.itemAbove?.state = State.LOADING
//                    adapter.apiInterface.getItemsWithOffset(
//                        category.id!!,
//                        (loadMore.itemAbove?.categoryBasedPosition).toString(),
//                        remaining
//                    )

                }
//
            }
        }
    }
}