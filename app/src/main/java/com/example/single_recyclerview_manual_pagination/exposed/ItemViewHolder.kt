package com.example.single_recyclerview_manual_pagination.exposed

import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

abstract class ItemViewHolder<T>(binding: ViewBinding, private val scope: CoroutineScope) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindItem(item as UiModel.Item<T>, adapter, position)
    }

    abstract var retryView: View

    companion object {
        private val count = AtomicInteger(0)
    }

    private fun bindItem(
        item: UiModel.Item<T>,
        adapter: CustomPagingAdapter<T>,
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
        adapter: CustomPagingAdapter<T>,
        item: UiModel.Item<T>,
        position: Int
    )

    private fun callApiAndMarkItemsAsLoading(
        adapter: CustomPagingAdapter<T>,
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

    abstract fun showPlaceholder(
        item: UiModel.Item<T>,
        adapter: CustomPagingAdapter<T>,
        position: Int
    )

    abstract fun showItem(item: UiModel.Item<T>, adapter: CustomPagingAdapter<T>, position: Int)
}
