package com.example.single_recyclerview_manual_pagination.exposed

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.State
import com.example.single_recyclerview_manual_pagination.models.UiModel
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int)
}

//abstract class AbstractViewHolders{
abstract class ItemViewHolder<T>(private val binding: ItemsBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindItem(item as UiModel.Item<T>, adapter, position)
    }

    companion object {
        private val count = AtomicInteger(0)
    }

    private fun bindItem(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int) {
        Log.i("onbind", "position: $position Item: $item")
        if (item.baseModelOfItem.item == null) {
            binding.retry.isVisible = false
            binding.itemImageView.isVisible = true
            showPlaceholder(item, adapter, position)
            if (item.baseModelOfItem.state == State.NOT_LOADING) {
                binding.retry.isVisible = false
                binding.itemImageView.isVisible = true
                if (item.baseModelOfItem.category != null && item.baseModelOfItem.category?.id != null) {
                    if (!item.baseModelOfItem.isLoadMoreClicked) {
                        callApiAndMarkItemsAsLoading(
                            adapter = adapter,
                            position = position,
                            id = item.baseModelOfItem.category?.id!!,
                            offset = (item.baseModelOfItem.categoryBasedPosition.plus(
                                1
                            )).toString(),
                            limit = item.baseModelOfItem.category?.initialCount!!,
                            item = item,
                            isLoadMoreClicked = false
                        )
                    } else if (item.baseModelOfItem.isLoadMoreClicked) {
                        val category =
                            adapter.dataset.listOfItems.find { it.id == item.baseModelOfItem.category?.id }
                        callApiAndMarkItemsAsLoading(
                            adapter = adapter,
                            position = position,
                            id = category?.id!!,
                            offset = (item.baseModelOfItem.categoryBasedPosition.plus(
                                1
                            )).toString(),
                            limit = category.itemsToLoadAfterViewMore,
                            item = item,
                            isLoadMoreClicked = true
                        )
                    }
                }
            }
            if (item.baseModelOfItem.state == State.ERROR) {
                Log.i("bindItem", "here")
                binding.retry.isVisible = true
                binding.itemImageView.isVisible = false
            }
        } else if (item.baseModelOfItem.state == State.LOADED) {
            binding.retry.isVisible = false
            binding.itemImageView.isVisible = true
            showItem(item, adapter, position)
        }
    }

    private fun callApiAndMarkItemsAsLoading(
        adapter: AbstractAdapter<T>,
        item: UiModel.Item<T>,
        position: Int,
        id: Int,
        offset: String,
        limit: Int,
        isLoadMoreClicked: Boolean
    ) {
        Log.i(
            "callApiAndMarkItems",
            "${count.incrementAndGet()} position${position} offset${offset}"
        )
        val category =
            adapter.dataset.listOfItems.find { it.id == item.baseModelOfItem.category?.id }
        if (category != null) {
            for (items in category.baseModelOfItemList) {
                items.isLoadMoreClicked = isLoadMoreClicked
                items.state = State.LOADING
            }
        }
        var i = position
        adapter.apiInterface.getItemsWithOffset(
            id,
            offset,
            limit
        )

    }

    abstract fun showPlaceholder(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
    abstract fun showItem(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class HeaderViewHolder<T>(binding: HeaderBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindHeader(item as UiModel.Header<T>, adapter, position)
    }

    abstract fun bindHeader(header: UiModel.Header<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class LoadMoreViewHolder<T>(private val binding: LoadMoreBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindLoadMore(item as UiModel.LoadMore<T>, adapter, position)
    }

    private fun bindLoadMore(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    ) {
        doStuffWithLoadMoreUI(loadMore, adapter, position)
        binding.loadMore.setOnClickListener {
            setClickListener(loadMore, adapter, position)
        }
    }

    abstract fun doStuffWithLoadMoreUI(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    )

    abstract fun doStuffWithOnClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    )

    private fun setClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    ) {
        val category = adapter.dataset.listOfItems.find { it.id == loadMore.id }
        loadMore.baseModelItemAbove?.isLoadMoreClicked = true
        if (category != null) {
            val remaining = category.itemsToLoadAfterViewMore
            val tempList = category.baseModelOfItemList.toMutableList()
            var j = 0
            repeat(remaining) {
                tempList.add(
                    BaseModelOfItem(
                        isLoadMoreClicked = true,
                        categoryBasedPosition = loadMore.baseModelItemAbove?.categoryBasedPosition!! + j
                    )
                )
                j++;
            }
            category.baseModelOfItemList = tempList
            val uiModelList =
                adapter.dataset.convertToUiModel()
            adapter.submitList(uiModelList)
            doStuffWithOnClickListener(loadMore, adapter, position)
        }
    }
}
//}
