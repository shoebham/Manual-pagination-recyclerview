package com.example.single_recyclerview_manual_pagination.exposed

import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class ItemViewHolder<T>(binding: ViewBinding, private val scope: CoroutineScope) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindItem(item as UiModel.Item<T>, adapter, position)
    }

    abstract var retryView: View

    companion object {
        private val count = AtomicInteger(0)
    }

    private fun bindItem(
        item: UiModel.Item<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    ) {
        Log.i("onbind", "position: $position Item: $item")

        if (item.baseModelOfItem.item == null) {
            showPlaceholder(item, adapter, position)
            isRetryVisible(false)
            val category =
                adapter.dataset.categoryList.find { it.id == item.baseModelOfItem.category?.id }

            if (category != null) {
                if (item.baseModelOfItem.state == State.NOT_LOADING) {
                    if (category.id != -1) {
                        if (!item.baseModelOfItem.isLoadMoreClicked) {
                            callApiAndMarkItemsAsLoading(
                                adapter = adapter,
                                position = position,
                                id = category.id,
                                offset = (item.baseModelOfItem.categoryBasedPosition.plus(
                                    1
                                )).toString(),
                                limit = category.initialCount,
                                item = item,
                                isLoadMoreClicked = false
                            )
                        } else if (item.baseModelOfItem.isLoadMoreClicked) {
                            callApiAndMarkItemsAsLoading(
                                adapter = adapter,
                                position = position,
                                id = category.id,
                                offset = (item.baseModelOfItem.categoryBasedPosition.plus(
                                    1
                                )).toString(),
                                limit = category.itemsToLoadAfterViewMore,
                                item = item,
                                isLoadMoreClicked = true
                            )
                        }
                    }

                } else if (item.baseModelOfItem.state == State.ERROR) {
                    Log.i("bindItem", "here")
                    isRetryVisible(true)
                    setRetryListener(adapter = adapter, item = item, position = position)
                    retryView.setOnClickListener {
                        isRetryVisible(false)
                        callApiAndMarkItemsAsLoading(
                            adapter = adapter,
                            position = position,
                            id = category.id,
                            offset = (item.baseModelOfItem.categoryBasedPosition.plus(
                                1
                            )).toString(),
                            limit = category.initialCount,
                            item = item,
                            isLoadMoreClicked = false
                        )
                    }
                }
            }
        } else if (item.baseModelOfItem.state == State.LOADED) {
            isRetryVisible(false)
            showItem(item, adapter, position)
        }
    }

    abstract fun isRetryVisible(isVisible: Boolean)
    abstract fun setRetryListener(
        adapter: AbstractAdapter<T>,
        item: UiModel.Item<T>,
        position: Int
    )

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
            adapter.dataset.categoryList.find { it.id == item.baseModelOfItem.category?.id }
        if (category != null) {
            for (items in category.baseModelOfItemList) {
                items.state = State.LOADED
                items.isLoadMoreClicked = isLoadMoreClicked
            }
        }
        var res: List<T>?
        scope.launch {
            res = adapter.apiInterface.getItemsWithOffset(
                id,
                offset,
                limit
            )
            adapter.dataset.mapListToBaseModelOfItem(res, id, offset, limit)
            adapter.submitList(adapter.dataset.convertToUiModel())
        }

    }

    abstract fun showPlaceholder(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
    abstract fun showItem(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class HeaderViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindHeader(item as UiModel.Header<T>, adapter, position)
    }

    abstract fun bindHeader(header: UiModel.Header<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class LoadMoreViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindLoadMore(item as UiModel.LoadMore<T>, adapter, position)
    }

    abstract var loadMoreView: View
    private fun bindLoadMore(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    ) {
        loadMoreView.isEnabled = false
        if (loadMore.baseModelItemAbove.item != null
            && loadMore.baseModelItemAbove.state == State.LOADED
        ) {
            loadMore.visible = !loadMore.baseModelItemAbove.isLastItem
            loadMoreView.isEnabled = true
        }
        loadMoreView.isVisible = loadMore.visible
        doStuffWithLoadMoreUI(loadMore, adapter, position)
        loadMoreView.setOnClickListener {
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
        val category = adapter.dataset.categoryList.find { it.id == loadMore.id }
        loadMore.baseModelItemAbove.isLoadMoreClicked = true
        if (category != null) {
            val itemsToLoadAfterViewMore = category.itemsToLoadAfterViewMore
            val tempList = category.baseModelOfItemList.toMutableList()
            var j = 0
            repeat(itemsToLoadAfterViewMore) {
                tempList.add(
                    BaseModelOfItem(
                        isLoadMoreClicked = true,
                        categoryBasedPosition =
                        loadMore.baseModelItemAbove.categoryBasedPosition + j,
                        category = category
                    )
                )
                j++
            }
            category.baseModelOfItemList = tempList
            val uiModelList = adapter.dataset.convertToUiModel()
            adapter.submitList(uiModelList)
            doStuffWithOnClickListener(loadMore, adapter, position)
        }
    }
}

