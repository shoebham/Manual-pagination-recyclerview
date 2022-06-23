package com.example.single_recyclerview_manual_pagination.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.exposed.CustomPagingAdapter
import com.example.single_recyclerview_manual_pagination.exposed.HeaderViewHolder
import com.example.single_recyclerview_manual_pagination.exposed.ItemViewHolder
import com.example.single_recyclerview_manual_pagination.exposed.LoadMoreViewHolder
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.exposed.UiModel
import com.example.single_recyclerview_manual_pagination.models.CategoryInheritingAbstractClass
import kotlinx.coroutines.CoroutineScope
import java.util.*
import java.util.concurrent.atomic.AtomicInteger


class Viewholders {

    companion object {
        private var count = AtomicInteger(0)
    }

    class StickerViewHolder(private val binding: ItemsBinding, scope: CoroutineScope) :
        ItemViewHolder<Sticker>(binding, scope = scope) {

        override var retryView: View = binding.retry

        companion object {
            fun from(parent: ViewGroup, scope: CoroutineScope): StickerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemsBinding.inflate(layoutInflater, parent, false)
                return StickerViewHolder(binding, scope)
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
            adapter: CustomPagingAdapter<Sticker>,
            position: Int
        ) {
            Glide.with(binding.root.context)
                .load(item.baseModelOfItem.item?.fixedWidthSmall?.png?.url)
                .placeholder(getRandomDrawableColor(position)).into(binding.itemImageView)
        }

        override fun showItem(
            item: UiModel.Item<Sticker>,
            adapter: CustomPagingAdapter<Sticker>,
            position: Int
        ) {
            Glide.with(binding.root.context)
                .load(item.baseModelOfItem.item?.fixedWidthSmall?.png?.url)
                .placeholder(getRandomDrawableColor(position)).into(binding.itemImageView)
        }


        override fun isRetryVisible(isVisible: Boolean) {
            binding.retry.isVisible = isVisible
        }

        override fun setRetryListener(
            adapter: CustomPagingAdapter<Sticker>,
            item: UiModel.Item<Sticker>,
            position: Int
        ) {
            retryView = binding.retry
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
            adapter: CustomPagingAdapter<Sticker>,
            position: Int
        ) {
            binding.textitem.text = (header.category as CategoryInheritingAbstractClass).name
        }

    }

    class loadmoreviewholder(private val binding: LoadMoreBinding) :
        LoadMoreViewHolder<Sticker>(binding) {
        override var loadMoreView: View = binding.loadMore
        companion object {
            fun from(parent: ViewGroup): loadmoreviewholder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LoadMoreBinding.inflate(layoutInflater, parent, false)
                return loadmoreviewholder(binding)
            }
        }

        override fun doStuffWithLoadMoreUI(
            loadMore: UiModel.LoadMore<Sticker>,
            adapter: CustomPagingAdapter<Sticker>,
            position: Int
        ) {

        }

        override fun doStuffWithOnClickListener(
            loadMore: UiModel.LoadMore<Sticker>,
            adapter: CustomPagingAdapter<Sticker>,
            position: Int
        ) {

        }
    }
}