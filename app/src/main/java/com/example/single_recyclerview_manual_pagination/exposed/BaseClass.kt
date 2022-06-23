package com.example.single_recyclerview_manual_pagination.exposed

import android.util.Log
import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItemInheritingAbstractClass


class BaseClass<T>(var listOfItems: List<Category<T>>) {


    var uiModelList = listOf<UiModel<T>>()

    fun convertToUiModel(): List<UiModel<T>> {
        val tempUiModelList = mutableListOf<UiModel<T>>()
        for ((i, item) in listOfItems.withIndex()) {
            tempUiModelList.add(UiModel.Header(item))
            for ((j, it) in item.baseModelOfItemList.withIndex()) {
                it.category = item
                tempUiModelList.add(UiModel.Item(it))
            }
            tempUiModelList.add(
                UiModel.LoadMore(
                    baseModelItemAbove = item.baseModelOfItemList.last(),
                    id = item.id,
                    visible = item.isViewMoreVisible
                )
            )
        }
        return tempUiModelList
    }

    private fun replacePlaceholders(
        it: List<BaseModelOfItem<T>>,
        id: Int,
        offset: String?,
    ) {

        val category = listOfItems.find { it.id == id }

        val itemlist = category?.baseModelOfItemList?.toMutableList()

        if (itemlist != null && itemlist[0].item == null) {
            listOfItems.find { it.id == id }?.baseModelOfItemList = it
        } else {
            if (itemlist != null && it.isNotEmpty()) {
                Log.i("baseclass", "category: $category it:${it.size}")
                for ((i, item) in it.withIndex()) {
                    itemlist[offset!!.toInt() + i - 1] = item
                }
                itemlist.removeAll { it.item == null }
                for (item in itemlist) {
                    item.state = State.LOADED
                }
                category.baseModelOfItemList = itemlist
                if (it.isEmpty()) category.baseModelOfItemList.last().isLastItem = true
            }
        }
    }

    fun mapListToBaseModelOfItem(
        response: List<T>?, id: Int, offset: String?,
        limit: Int?
    ) {
        val tempBaseModelItemList =
            mutableListOf<BaseModelOfItem<T>>()
        if (response != null && response.isNotEmpty() && response[0] != null) {
            for ((i, item) in response.withIndex()) {
                tempBaseModelItemList.add(
                    BaseModelOfItem(
                        item,
                        categoryBasedPosition = offset!!.toInt() + i,
                        state = State.LOADED,
                        isLastItem = response.size < limit!!,
                    )
                )
            }
        } else if (response == null) {
            for (i in 0 until limit!! + 1) {
                tempBaseModelItemList.add(
                    BaseModelOfItem(
                        null,
                        state = State.ERROR
                    )
                )
            }
        }
        replacePlaceholders(it = tempBaseModelItemList, id = id, offset = offset)
    }

    fun getCategoryPositionInUiModelList(id: Int?): Int {
        for ((i, item) in uiModelList.withIndex()) {
            if (item is UiModel.Header) {
                if (item.category.id == id)
                    return i
            }
        }
        return 0
    }

    fun getCategoryIdxOfCurrentPosition(position: Int): Int {
        var j = -1
        for ((i, item) in uiModelList.withIndex()) {
            if (item is UiModel.Header) j++
            if (i == position) {
                return j
            }
        }
        return 0
    }

}