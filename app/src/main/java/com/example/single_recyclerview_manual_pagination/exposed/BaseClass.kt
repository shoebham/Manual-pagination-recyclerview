package com.example.single_recyclerview_manual_pagination.exposed

import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItemInheritingAbstractClass


class BaseClass<T>(var listOfItems: List<Category<T>>) : List<Any> {


    var uiModelList = listOf<UiModel<T>>()
    override val size: Int = uiModelList.size

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

    fun replacePlaceholders(
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
        response: List<T?>, id: Int, offset: String?,
        limit: Int?
    ) {
        val tempBaseModelItemList =
            mutableListOf<BaseModelOfItem<T>>()
        if (response.isNotEmpty() && response[0] != null) {
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
        } else {
            for (i in 0 until limit!!) {
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

    override fun contains(element: Any): Boolean {
        return uiModelList.contains(element)
    }

    override fun containsAll(elements: Collection<Any>): Boolean {
        return uiModelList.containsAll(elements)
    }

    override fun get(index: Int): Any {
        return uiModelList[index]
    }

    override fun indexOf(element: Any): Int {
        return uiModelList.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return uiModelList.isEmpty()
    }

    override fun iterator(): Iterator<Any> {
        return uiModelList.iterator()
    }

    override fun lastIndexOf(element: Any): Int {
        return uiModelList.lastIndexOf(element)
    }

    override fun listIterator(): ListIterator<Any> {
        return uiModelList.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<Any> {
        return uiModelList.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Any> {
        return uiModelList.subList(fromIndex, toIndex)
    }
}