package com.example.single_recyclerview_manual_pagination.exposed


/**
 * A wrapper class of type T that contains category list and Ui Model List.
 * category list is used to perform various operations when the data is recieved from the server.
 * ui model list is used to show the data in the recycler view.
 */
class PagingListWrapperClass<T>(var categoryList: List<Category<T>>) {

    var uiModelList = listOf<UiModel<T>>()

    /**
     * Converts the current category list to UiModelList which is shown in the recyclerview
     * Assumes Header is the first item then items below it and load more below the items
     *
     * ............
     *
     * Header 1
     *
     * Item 1
     *
     * Item 2
     *
     * Item 3
     *
     * Load More
     *
     * ............
     *
     * This list should be submitted to the recyclerview and should be called whenever
     * new data needs to shown
     */
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

    /** Replaces the placeholders that are shown initially with the data received from the API
     * if Load more is not clicked the list initially shown in the category is replaced with the
     * data received from the API
     * if load more is clicked then initially null data is appended till the limit parameter
     * in the list of items inside the category
     * as soon as data is received the empty data is then replaced with the
     * data received from the API and if the data received is less than the limit
     * then the null items are removed from the list
     *
     * @param listOfBaseModelOfItemFromAPI the list of items received from the API
     * @param id the id of the category
     * @param offset the offset passed in the API call
     */
    private fun replacePlaceholders(
        listOfBaseModelOfItemFromAPI: List<BaseModelOfItem<T>>,
        id: Int,
        offset: String,
    ) {

        val category = categoryList.find { it.id == id }
        val itemList = category?.baseModelOfItemList?.toMutableList()
        if (itemList != null && itemList[0].item == null) {
            categoryList.find { it.id == id }?.baseModelOfItemList = listOfBaseModelOfItemFromAPI
        } else {
            if (itemList != null && listOfBaseModelOfItemFromAPI.isNotEmpty()) {
                for ((i, item) in listOfBaseModelOfItemFromAPI.withIndex()) {
                    itemList[offset.toInt() + i - 1] = item
                }
                itemList.removeAll { it.item == null }
                category.baseModelOfItemList = itemList
                if (listOfBaseModelOfItemFromAPI.isEmpty()) category.baseModelOfItemList.last().isLastItem =
                    true
            }
        }
    }

    /**
     * Maps the server response to the list of items that are shown in the UI
     * finds the category in the [categoryList] and puts that category in the item while wrapping
     * the response in [BaseModelOfItem]
     * If null is received as the response then it is assumed that there was an Error and
     * Error state is set in the item
     *
     * @param response the response received from the server
     * @param id the id of the category
     * @param offset the offset passed in the API call
     * @param limit the limit passed in the API call
     */
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

        replacePlaceholders(
            listOfBaseModelOfItemFromAPI = tempBaseModelItemList,
            id = id,
            offset = offset
        )
    }


    /**
     * Gets the category position in the current UiModelList
     * @param id the category id
     * @return the category position
     */
    fun getCategoryPositionInUiModelList(id: Int): Int {
        uiModelList.find { it is UiModel.Header && it.category.id == id }?.let {
            return uiModelList.indexOf(it)
        }
        return 0
    }

    /**
     * Gets the category index sequentially by iterating over all the categories and
     * returning the correct category number at a given position
     * for ex. if we have 10 categories and each of them have 40 items,
     * position[0-40] will lie in idx 0, [41-80] will lie in idx 1 and so on
     * item position 120 in the list will return the category index 2
     */
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