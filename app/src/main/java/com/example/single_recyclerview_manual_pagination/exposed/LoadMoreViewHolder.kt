package com.example.single_recyclerview_manual_pagination.exposed

import android.view.View
import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding


/**
 * Abstract class to provide support for Load More Button in vertical scrolling recycler view.
 * Assumes the support of view binding
 */
abstract class LoadMoreViewHolder<T>(binding: ViewBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindLoadMore(item as UiModel.LoadMore<T>, adapter)
    }

    /**
     * Binds the load more button should be overridden by the subclass and given the correct view
     * as on click listener is set on this view in the module
     */
    abstract var loadMoreView: View

    /**
     * Binds the load more button and provides some default behaviour like the button is disabled
     * in the start when the data is being loaded and the button is enabled when the data is loaded
     * and sets the visibility according to the state of the items above
     * Sets the default behavior of the click listener to load more
     */
    private fun bindLoadMore(
        loadMore: UiModel.LoadMore<T>,
        adapter: CustomPagingAdapter<T>
    ) {
        loadMoreView.isEnabled = false
        if (loadMore.baseModelItemAbove.item != null
            && loadMore.baseModelItemAbove.state == State.LOADED
        ) {
            loadMore.visible = !loadMore.baseModelItemAbove.isLastItem
            loadMoreView.isEnabled = true
        }
        loadMoreView.isVisible = loadMore.visible
        doStuffWithLoadMoreUI(loadMore)
        loadMoreView.setOnClickListener {
            setClickListener(loadMore, adapter)
        }
    }

    abstract fun doStuffWithLoadMoreUI(
        loadMore: UiModel.LoadMore<T>
    )

    abstract fun doStuffWithOnClickListener(
        loadMore: UiModel.LoadMore<T>
    )

    /**
     * When load more is clicked until the data is loaded, the UiModel list is filled with
     * null items from the position of the load more button.
     * This function finds the category of the load more button and adds the items to the
     * item list present in that category and sets isLoadMoreClicked to true on the item just above
     * load more button
     * after filling the data with null items, the adapter is notified to update the list
     */
    private fun setClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: CustomPagingAdapter<T>
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
            doStuffWithOnClickListener(loadMore)
        }
    }
}

