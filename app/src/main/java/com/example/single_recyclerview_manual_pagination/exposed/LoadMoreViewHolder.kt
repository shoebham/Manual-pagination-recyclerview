package com.example.single_recyclerview_manual_pagination.exposed

import android.view.View
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding


abstract class LoadMoreViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindLoadMore(item as UiModel.LoadMore<T>, adapter, position)
    }

    abstract var loadMoreView: View
    private fun bindLoadMore(
        loadMore: UiModel.LoadMore<T>,
        adapter: CustomPagingAdapter<T>,
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
        adapter: CustomPagingAdapter<T>,
        position: Int
    )

    abstract fun doStuffWithOnClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: CustomPagingAdapter<T>,
        position: Int
    )

    private fun setClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: CustomPagingAdapter<T>,
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

