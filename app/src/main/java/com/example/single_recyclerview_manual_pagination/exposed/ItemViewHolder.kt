package com.example.single_recyclerview_manual_pagination.exposed

import android.util.Log
import android.view.View
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger


/**
 * Abstract class to provide support for Items in vertical scrolling recycler view.
 * Assumes the support of view binding
 */
abstract class ItemViewHolder<T>(binding: ViewBinding, private val scope: CoroutineScope) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: CustomPagingAdapter<T>, position: Int) {
        bindItem(item as UiModel.Item<T>, adapter, position)
    }

    /**
     * Binds the retry button, should be overridden by the subclass and given the correct view
     * as on click listener is set on this view in the module
     */
    abstract var retryView: View

    companion object {
        private val count = AtomicInteger(0)
    }

    /**
     * Binds the item to the view holder.
     * This method first checks if the current item is null or not, if it is null then it shows
     * a placeholder, and checks its state
     * if it is NOT_LOADING then it calls the [callApiAndMarkItemsAsLoading]with correct parameters,
     * if the state is LOADING then it calls [showItem] to show the correct item
     * if the state is ERROR then it sets retry button as visible and sets click listener on the
     * button to call [callApiAndMarkItemsAsLoading] with correct parameters
     * @param item the item to be bound to the view holder
     * @param adapter the adapter that is currently bound to the view holder
     * @param position the position of the item in the adapter
     */
    private fun bindItem(
        item: UiModel.Item<T>,
        adapter: CustomPagingAdapter<T>,
        position: Int
    ) {
        Log.i("onbind", "position: $position Item: $item")
        if (item.baseModelOfItem.item == null) {
            showPlaceholder(item)
            isRetryVisible(false)
            val category =
                adapter.dataset.categoryList.find { it.id == item.baseModelOfItem.category?.id }

            if (category != null) {
                if (item.baseModelOfItem.state == State.NOT_LOADING) {
                    if (category.id != -1) {
                        if (!item.baseModelOfItem.isLoadMoreClicked) {
                            callApiAndMarkItemsAsLoading(
                                adapter = adapter,
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
                    setRetryListener(item = item)
                    retryView.setOnClickListener {
                        isRetryVisible(false)
                        callApiAndMarkItemsAsLoading(
                            adapter = adapter,
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
            showItem(item)
        }
    }

    /**
     * sets the retry button as visible or not visible
     */
    abstract fun isRetryVisible(isVisible: Boolean)

    /**
     * a function the user can override to set the retry button click listener
     */
    abstract fun setRetryListener(
        item: UiModel.Item<T>
    )

    /**
     * Calls the api with the correct parameters and marks the item as LOADED, it is assumed that
     * the api call is successful, if it is not successful then the item is marked as ERROR in
     * [PagingListWrapperClass]
     * The API call is launched in the Coroutine scope passed in the parameter of the viewholder
     * after the data is received from the server the items are then first converted to
     * [BaseModelOfItem] and all the changes are reflected in the category list of the adapter
     * after the changes the the category list is converted to UI Model, and adapter is notified of
     * the changes by submitList()
     * @param adapter the adapter that is currently bound to the view holder
     * @param id the id of the category
     * @param offset the offset of the category
     * @param limit the limit of the category
     * @param item the item that is currently bound to the view holder
     * @param isLoadMoreClicked true if the user clicked on the load more button, false otherwise
     */
    private fun callApiAndMarkItemsAsLoading(
        adapter: CustomPagingAdapter<T>,
        item: UiModel.Item<T>,
        id: Int,
        offset: String,
        limit: Int,
        isLoadMoreClicked: Boolean
    ) {
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
        item: UiModel.Item<T>
    )

    abstract fun showItem(item: UiModel.Item<T>)
}
