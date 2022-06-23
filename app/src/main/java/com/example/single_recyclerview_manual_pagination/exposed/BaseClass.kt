package com.example.single_recyclerview_manual_pagination.exposed


class BaseClass<T>(var categoryList: List<Category<T>>) {


    var uiModelList = listOf<UiModel<T>>()

    fun convertToUiModel(): List<UiModel<T>> {
        val tempUiModelList = mutableListOf<UiModel<T>>()
        for (item in categoryList) {
            tempUiModelList.add(UiModel.Header(item))
            for (it in item.baseModelOfItemList) {
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
        offset: String,
    ) {

        val category = categoryList.find { it.id == id }
        val itemlist = category?.baseModelOfItemList?.toMutableList()
        if (itemlist != null && itemlist[0].item == null) {
            categoryList.find { it.id == id }?.baseModelOfItemList = it
        } else {
            if (itemlist != null && it.isNotEmpty()) {
                for ((i, item) in it.withIndex()) {
                    itemlist[offset.toInt() + i - 1] = item
                }
                itemlist.removeAll { it.item == null }
                category.baseModelOfItemList = itemlist
                if (it.isEmpty()) category.baseModelOfItemList.last().isLastItem = true
            }
        }
    }

    fun mapListToBaseModelOfItem(
        response: List<T>?, id: Int, offset: String,
        limit: Int
    ) {
        val tempBaseModelItemList =
            mutableListOf<BaseModelOfItem<T>>()
        val category = categoryList.find { it.id == id }
        if (category != null) {
            if (response != null && response.isNotEmpty() && response[0] != null) {
                for ((i, item) in response.withIndex()) {
                    tempBaseModelItemList.add(
                        BaseModelOfItem(
                            item,
                            categoryBasedPosition = offset.toInt() + i,
                            state = State.LOADED,
                            isLastItem = response.size < limit,
                            category = category
                        )
                    )
                }
            } else if (response == null) {
                for (i in 0 until limit) {
                    tempBaseModelItemList.add(
                        BaseModelOfItem(
                            null,
                            state = State.ERROR,
                            category = category
                        )
                    )
                }
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