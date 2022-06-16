package com.example.single_recyclerview_manual_pagination.exposed

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.single_recyclerview_manual_pagination.databinding.HeaderBinding
import com.example.single_recyclerview_manual_pagination.databinding.ItemsBinding
import com.example.single_recyclerview_manual_pagination.databinding.LoadMoreBinding
import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItemInheritingAbstractClass
import com.example.single_recyclerview_manual_pagination.models.State
import com.example.single_recyclerview_manual_pagination.models.UiModel

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int)
}

//abstract class AbstractViewHolders{
abstract class ItemViewHolder<T>(private val binding: ItemsBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindItem(item as UiModel.Item<T>, adapter, position)
    }

    private fun bindItem(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int) {
        if (item.baseModelOfItem.item == null) {
            showPlaceholder(item, adapter, position)
            if (item.baseModelOfItem.state == State.NOT_LOADING) {
                if (item.baseModelOfItem.category != null && item.baseModelOfItem.category?.id != null) {
                    if (!item.baseModelOfItem.isLoadMoreClicked) {
                        callApiAndMarkItemsAsLoading(
                            adapter, position,
                            id = item.baseModelOfItem.category?.id!!,
                            offset = (item.baseModelOfItem.categoryBasedPosition?.plus(
                                1
                            )).toString(),
                            limit = item.baseModelOfItem.category?.initialCount!!,
                            false
                        )
                    } else if (item.baseModelOfItem.isLoadMoreClicked) {
                        val category =
                            adapter.dataset.listOfItems.find { it.id == item.baseModelOfItem.category?.id }
                        callApiAndMarkItemsAsLoading(
                            adapter, position,
                            id = category?.id!!,
                            offset = (item.baseModelOfItem.categoryBasedPosition?.plus(
                                1
                            )).toString(),
                            limit = category.itemsToLoadAfterViewMore,
                            true
                        )
                    }
                }
            }
        } else if (item.baseModelOfItem.state == State.LOADED) {
            showItem(item, adapter, position)
        }
    }

    private fun callApiAndMarkItemsAsLoading(
        adapter: AbstractAdapter<T>,
        position: Int,
        id: Int,
        offset: String,
        limit: Int,
        isLoadMoreClicked: Boolean
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
            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItem.isLoadMoreClicked =
                isLoadMoreClicked
            i++
        }
    }

    abstract fun showPlaceholder(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
    abstract fun showItem(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int)
}

abstract class HeaderViewHolder<T>(binding: HeaderBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindHeader(item as UiModel.Header<T>)
    }

    abstract fun bindHeader(header: UiModel.Header<T>)
}

abstract class LoadMoreViewHolder<T>(private val binding: LoadMoreBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bindLoadMore(item as UiModel.LoadMore<T>, adapter, position)
    }

    fun bindLoadMore(loadMore: UiModel.LoadMore<T>, adapter: AbstractAdapter<T>, position: Int) {
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
        loadMore.itemInheritingAbstractClassAbove?.isLoadMoreClicked = true
        if (category != null) {
            val remaining = category.itemsToLoadAfterViewMore
            val tempList = category.baseModelOfItemList.toMutableList()
            var j = 0
            repeat(remaining) {
                tempList.add(
                    BaseModelOfItemInheritingAbstractClass(
                        isLoadMoreClicked = true,
                        categoryBasedPosition = loadMore.itemInheritingAbstractClassAbove?.categoryBasedPosition!! + j
                    )
                )
                j++;
            }
            category.baseModelOfItemList = tempList
            val uiModellist =
                adapter.dataset.convertToUiModel()
            adapter.submitList(uiModellist)
            doStuffWithOnClickListener(loadMore, adapter, position)
        }
    }
}
//}
