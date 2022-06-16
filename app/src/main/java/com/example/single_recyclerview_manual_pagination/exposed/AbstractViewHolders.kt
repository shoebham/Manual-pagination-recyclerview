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
        bind(item, adapter, position)
    }

    fun bind(item: UiModel.Item<T>, adapter: AbstractAdapter<T>, position: Int) {
        if (item.baseModelOfItemInheritingAbstractClass.item == null) {
            showPlaceholder()
            if (item.baseModelOfItemInheritingAbstractClass.state == State.NOT_LOADING) {
                if (item.baseModelOfItemInheritingAbstractClass.category != null && item.baseModelOfItemInheritingAbstractClass.category?.id != null) {
                    if (!item.baseModelOfItemInheritingAbstractClass.isLoadMoreClicked) {
                        callApiAndMarkItemsAsLoading(
                            adapter, position,
                            id = item.baseModelOfItemInheritingAbstractClass.category?.id!!,
                            offset = (item.baseModelOfItemInheritingAbstractClass.categoryBasedPosition?.plus(
                                1
                            )).toString(),
                            limit = item.baseModelOfItemInheritingAbstractClass.category?.initialCount!!,
                            false
                        )
                    } else if (item.baseModelOfItemInheritingAbstractClass.isLoadMoreClicked) {
                        val category =
                            adapter.dataset.listOfItems.find { it.id == item.baseModelOfItemInheritingAbstractClass.category?.id }
                        callApiAndMarkItemsAsLoading(
                            adapter, position,
                            id = category?.id!!,
                            offset = (item.baseModelOfItemInheritingAbstractClass.categoryBasedPosition?.plus(
                                1
                            )).toString(),
                            limit = category.itemsToLoadAfterViewMore,
                            true
                        )
                    }
                }
            } else if (item.baseModelOfItemInheritingAbstractClass.state == State.LOADED) {
                showItem()
            }
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
            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItemInheritingAbstractClass.state =
                State.LOADING
            (adapter.differ.currentList[i] as UiModel.Item).baseModelOfItemInheritingAbstractClass.isLoadMoreClicked =
                isLoadMoreClicked
            i++
        }
    }

    abstract fun showPlaceholder()
    abstract fun showItem()
}

abstract class HeaderViewHolder<T>(private val binding: HeaderBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bind(item, adapter, position)
    }

    abstract fun bind(header: UiModel.Header<T>)
}

abstract class LoadMoreViewHolder<T>(private val binding: LoadMoreBinding) :
    BaseViewHolder<T>(binding.root) {
    override fun bind(item: UiModel<T>, adapter: AbstractAdapter<T>, position: Int) {
        bind(item, adapter, position)
    }

    fun bind(loadMore: UiModel.LoadMore<T>, adapter: AbstractAdapter<T>, position: Int) {
        binding.loadMore.setOnClickListener {
            setClickListener(loadMore, adapter, position)
        }
    }

    fun setClickListener(
        loadMore: UiModel.LoadMore<T>,
        adapter: AbstractAdapter<T>,
        position: Int
    ) {
        val category = adapter.dataset.listOfItems.find { it.id == loadMore.id }
        loadMore.itemInheritingAbstractClassAbove?.isLoadMoreClicked = true
        if (category != null) {
            val remaining = category.itemsToLoadAfterViewMore
            val tempList = category.itemInheritingAbstractClassList.toMutableList()
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
            category.itemInheritingAbstractClassList = tempList
            val uiModellist =
                adapter.apiInterface.convertToUiModel(
                    adapter.dataset,
                    adapter.dataset.listOfItems
                )
            adapter.submitList(uiModellist)
        }
    }
}
//}
