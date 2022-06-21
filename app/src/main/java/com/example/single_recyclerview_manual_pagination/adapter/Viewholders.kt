package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.R
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.exposed.AbstractAdapter
import com.example.single_recyclerview_manual_pagination.exposed.HeaderViewHolder
import com.example.single_recyclerview_manual_pagination.exposed.ItemViewHolder
import com.example.single_recyclerview_manual_pagination.exposed.LoadMoreViewHolder
import com.example.single_recyclerview_manual_pagination.models.State
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.UiModel
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class Viewholders {

    companion object {
        private var count = AtomicInteger(0)
    }

    class StickerViewHolder(private val binding: ItemsBinding) : ItemViewHolder<Sticker>(binding) {

        companion object {
            fun from(parent: ViewGroup): StickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemsBinding.inflate(layoutInflater, parent, false)
                return StickerViewHolder(binding)
            }

            var colorList = MutableList<ColorDrawable?>(10000) { null }

        }

        private fun getRandomDrawableColor(position: Int): ColorDrawable {
            if (colorList[position] == null) {
                Log.i("colors", "${count.incrementAndGet()}")
                val idx = Random().nextInt(vibrantLightColorList.size)
                val rnd = Random()
                val color = ColorDrawable(
                    Color.argb(
                        255,
                        rnd.nextInt(255),
                        rnd.nextInt(255),
                        rnd.nextInt(255)
                    )
                )
                colorList[position] = color
            }
            return colorList[position]!!
        }

        private val vibrantLightColorList = arrayOf(
            ColorDrawable(Color.parseColor("#9ACCCD")), ColorDrawable(Color.parseColor("#8FD8A0")),
            ColorDrawable(Color.parseColor("#CBD890")), ColorDrawable(Color.parseColor("#DACC8F")),
            ColorDrawable(Color.parseColor("#D9A790")), ColorDrawable(Color.parseColor("#D18FD9")),
            ColorDrawable(Color.parseColor("#FF6772")), ColorDrawable(Color.parseColor("#DDFB5C"))
        )


        override fun showPlaceholder(
            item: UiModel.Item<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {
            Glide.with(binding.root.context).load(getRandomDrawableColor(position))
                .into(binding.itemImageView)
            val rnd = Random()
            val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
//            binding.itemImageView.setColorFilter(color)
        }

        override fun showItem(
            item: UiModel.Item<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {
            Glide.with(binding.root.context)
                .load(item.baseModelOfItem.item?.fixedWidthFull?.png?.url)
                .placeholder(getRandomDrawableColor(position)).into(binding.itemImageView)
        }


//        fun bind(sticker: UiModel.Item<Sticker>, adapter: CustomAdapter<Sticker>, position: Int) {
//            if (sticker.baseModelOfItemInheritingAbstractClass.item == null) {
////                Glide.with(binding.root.context).load(R.drawable.placeholder)
//////                    .into(binding.itemImageView)
////                Glide.with(binding.root.context).load(R.drawable.placeholder)
////                    .into(binding.itemImageView)
////                val rnd = Random()
////                val color = Color.argb(255, rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255))
////                binding.itemImageView.setColorFilter(color)
////                sticker.baseModelOfItem.state = State.LOADING
//
//                if (sticker.baseModelOfItemInheritingAbstractClass.state == State.NOT_LOADING && !sticker.baseModelOfItemInheritingAbstractClass.isLoadMoreClicked) {
//                    if (sticker.baseModelOfItemInheritingAbstractClass.category != null && sticker.baseModelOfItemInheritingAbstractClass.category?.id != null) {
//                        callBindOnVisibleScreen(
//                            adapter, position,
//                            id = sticker.baseModelOfItemInheritingAbstractClass.category?.id!!,
//                            offset = (sticker.baseModelOfItemInheritingAbstractClass.categoryBasedPosition?.plus(
//                                1
//                            )).toString(),
//                            limit = sticker.baseModelOfItemInheritingAbstractClass.category?.initialCount!!
//                        )
////                        for (i in position until position + sticker.baseModelOfItem.category?.initialCount!!) {
////                            if (adapter.differ.currentList[i] is UiModel.Item) {
////                                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
////                                    State.LOADING
////                            }
////                        }
//                        sticker.baseModelOfItemInheritingAbstractClass.state = State.LOADING
//                    }
//                } else if (sticker.baseModelOfItemInheritingAbstractClass.isLoadMoreClicked) {
//                    val category =
//                        adapter.dataset.listOfItems.find { it.id == sticker.baseModelOfItemInheritingAbstractClass.category?.id }
//                    if (sticker.baseModelOfItemInheritingAbstractClass.state == State.NOT_LOADING) {
//                        callBindOnVisibleScreen(
//                            adapter, position,
//                            id = category?.id!!,
//                            offset = (sticker.baseModelOfItemInheritingAbstractClass.categoryBasedPosition?.plus(
//                                1
//                            )).toString(),
//                            limit = category.itemsToLoadAfterViewMore
//                        )
//                        var i = position
//                        while (adapter.differ.currentList[i] is UiModel.Item) {
//                            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItemInheritingAbstractClass.isLoadMoreClicked =
//                                true
//                            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItemInheritingAbstractClass.state =
//                                State.LOADING
//                            i++
//                        }
//                    }
//                }
//            } else if (sticker.baseModelOfItemInheritingAbstractClass.state == State.LOADED) {
//                Glide.with(binding.root.context)
//                    .load(sticker.baseModelOfItemInheritingAbstractClass.item?.fixedWidthFull?.png?.url)
//                    .placeholder(R.drawable.placeholder).into(binding.itemImageView)
//                binding.itemImageView.setColorFilter(null)
////                sticker.baseModelOfItem.state = State.LOADED
//            }
//        }

//        fun callBindOnVisibleScreen(
//            adapter: CustomAdapter<Sticker>,
//            position: Int,
//            id: Int,
//            offset: String,
//            limit: Int
//        ) {
//            adapter.apiInterface.getItemsWithOffset(
//                id,
//                offset,
//                limit
//            )
//            var i = position
//            while (adapter.differ.currentList[i] is UiModel.Item) {
//                (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItemInheritingAbstractClass.state =
//                    State.LOADING
//                i++
//            }
//
//        }

        fun bindBanner(item: UiModel.Banner<Sticker>) {
            Glide.with(binding.root.context)
                .load(item.url)
                .placeholder(R.drawable.placeholder).into(binding.itemImageView)
            binding.itemImageView.setColorFilter(null)
        }
    }

    class StickerHeaderViewHolder(private val binding: HeaderBinding) :
        HeaderViewHolder<Sticker>(binding) {
        companion object {
            fun from(parent: ViewGroup): StickerHeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HeaderBinding.inflate(layoutInflater, parent, false)
                return StickerHeaderViewHolder(binding)
            }
        }

        //        fun bind(stickerHeader: String?) {
//            binding.textitem.text = stickerHeader
//        }
        override fun bindHeader(
            header: UiModel.Header<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {
            binding.textitem.text = header.category?.name
        }

    }

    class loadmoreviewholder(private val binding: LoadMoreBinding) :
        LoadMoreViewHolder<Sticker>(binding) {
        companion object {
            fun from(parent: ViewGroup): loadmoreviewholder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoadMoreBinding.inflate(layoutInflater, parent, false)
                return loadmoreviewholder(binding)
            }
        }
//        fun bind(
//            loadMore: UiModel.LoadMore<Sticker>,
//            adapter: CustomAdapter<Sticker>,
//            position: Int
//        ) {
//            if (loadMore.itemInheritingAbstractClassAbove != null) {
//                loadMore.visible = !loadMore.itemInheritingAbstractClassAbove.isLastItem
//            }
//            binding.loadMore.isVisible = loadMore.visible
//            binding.loadMore.setOnClickListener {
//                val category = adapter.dataset.listOfItems.find { it.id == loadMore.id }
//                loadMore.itemInheritingAbstractClassAbove?.isLoadMoreClicked = true
//                if (category != null) {
//                    val remaining = category.itemsToLoadAfterViewMore
//                    var tempList = mutableListOf<BaseModelOfItemInheritingAbstractClass<Sticker>>()
//                    tempList = category.itemInheritingAbstractClassList.toMutableList()
//                    var j = 0
//                    repeat(remaining) {
//                        tempList.add(
//                            BaseModelOfItemInheritingAbstractClass(
//                                isLoadMoreClicked = true,
//                                categoryBasedPosition = loadMore.itemInheritingAbstractClassAbove?.categoryBasedPosition!! + j
//                            )
//                        )
//                        j++;
//                    }
//                    category.itemInheritingAbstractClassList = tempList
//                    val uiModellist =
//                        adapter.convertToUiModel(adapter.dataset, adapter.dataset.listOfItems)
//                    var i = position
//                    adapter.submitList(uiModellist)
//
////                    while (adapter.differ.currentList[i] is UiModel.Item) {
////                        (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.isLoadMoreClicked =
////                            true
////                        (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.state =
////                            State.LOADING
////                        i++
////                    }
//                    Log.i("baseclass", "${adapter.dataset}")
////                    loadMore.itemAbove?.state = State.LOADING
////                    adapter.apiInterface.getItemsWithOffset(
////                        category.id!!,
////                        (loadMore.itemAbove?.categoryBasedPosition).toString(),
////                        remaining
////                    )
//
//                }
////
//            }
//        }

        override fun doStuffWithLoadMoreUI(
            loadMore: UiModel.LoadMore<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {
            if (loadMore.baseModelItemAbove != null) {
                if (loadMore.baseModelItemAbove.state == State.ERROR) {
                    binding.loadMore.isEnabled = false
                } else {
                    loadMore.visible = !loadMore.baseModelItemAbove.isLastItem
                    binding.loadMore.isEnabled = true
                }
            }
            binding.loadMore.isVisible = loadMore.visible
        }

        override fun doStuffWithOnClickListener(
            loadMore: UiModel.LoadMore<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {

        }
    }
}