package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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
import com.example.single_recyclerview_manual_pagination.exposed.State
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.exposed.UiModel
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

            var colorMap = HashMap<Int, ColorDrawable>()
        }

        private fun getRandomDrawableColor(position: Int): ColorDrawable {
            if (colorMap.get(position) == null) {
//                Log.i("colors", "${count.incrementAndGet()}")
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
                colorMap.put(position, color)
            }
            return colorMap[position]!!
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
            Glide.with(binding.root.context)
                .load(getRandomDrawableColor(position))
                .into(binding.itemImageView)
        }

        override fun showItem(
            item: UiModel.Item<Sticker>,
            adapter: AbstractAdapter<Sticker>,
            position: Int
        ) {
            Glide.with(binding.root.context)
                .load(item.baseModelOfItem.item?.fixedWidthTiny?.png?.url)
                .placeholder(getRandomDrawableColor(position)).into(binding.itemImageView)
        }


        fun bindBanner(item: UiModel.Banner<Sticker>) {
            Glide.with(binding.root.context)
                .load(item.url)
                .placeholder(R.drawable.placeholder).into(binding.itemImageView)
            binding.itemImageView.setColorFilter(null)
        }

        override fun isRetryVisible(isVisible: Boolean) {
            binding.retry.isVisible = isVisible
        }

        override fun setRetryListener(
            adapter: AbstractAdapter<Sticker>,
            item: UiModel.Item<Sticker>,
            position: Int
        ) {
            retryView = binding.retry
//            binding.retry.setOnClickListener {
//                isRetryVisible(false)
//                callApiAndMarkItemsAsLoading(
//                    adapter = adapter,
//                    position = position,
//                    id = item.baseModelOfItem.category?.id!!,
//                    offset = (item.baseModelOfItem.categoryBasedPosition.plus(
//                        1
//                    )).toString(),
//                    limit = item.baseModelOfItem.category?.initialCount!!,
//                    item = item,
//                    isLoadMoreClicked = false
//                )
//            }
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
            binding.textitem.text = header.category.name
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