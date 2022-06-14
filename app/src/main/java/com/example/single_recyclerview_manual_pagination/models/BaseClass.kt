package com.example.single_recyclerview_manual_pagination.models

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay


abstract class Wrapper<T>() {
    abstract var listOfItems: List<Category<T>>
}

class BaseClass<T>(override var listOfItems: List<Category<T>>) : Wrapper<T>(), List<Any> {


    var mainList = listOf<UiModel<T>>()
    var listWrapper = mutableListOf<Any>()
    override val size: Int = listWrapper.size
    val lastAccessedPosition: Int = 0
    enum class Item_type {
        BANNER, ITEM, AD
    }

    fun submitList(list: Wrapper<T>, itemType: Item_type) {
        val modelList = mutableListOf<UiModel<T>>()
        val wrapper = mutableListOf<Any>()
        if (itemType == Item_type.ITEM) {
            for (items in list.listOfItems) {
                modelList.add(UiModel.Header(items.name))
                wrapper.add(items)
                for (item in items.itemList) {
                    modelList.add(UiModel.Item(item))
                    if (item != null) {
                        wrapper.add(item)
                    }
                }
            }
        }
        listWrapper = wrapper
        mainList = modelList
    }

    //    var currentCategory: Category<T> = categoryList[0]
    var counter = 0

//    fun convertToUiModelList(categoryList: List<Category<T>>): List<UiModel> {
//        val uiModelList = mutableListOf<UiModel>()
//        for (category in categoryList) {
//            uiModelList.add(UiModel.Header(category.name))
//            for (item in category.itemList) {
//                uiModelList.add(UiModel.Item(item as Sticker?))
//            }
//        }
//        return uiModelList
//    }

//    fun getAt(itemPosition: Int): UiModel {
////        if(itemPosition==0)return 0
//        var i = 0
//        var totalTillNow = categoryList[i].itemList.size + 1
//        var itemPositionToReturn = itemPosition
//        while (i < categoryList.size - 1 && itemPositionToReturn >= totalTillNow) {
//            itemPositionToReturn -= totalTillNow
//            i++;
//            totalTillNow = (categoryList[i].itemList.size + 1)
////            currentCategory=categoryList[i]
//        }
//        itemPositionToReturn -= 1
//        counter = i
////        if(itemPositionToReturn==0) header
////        if(itemPositionToReturn==-1) loadmore
////        currentCategory=categoryList[i]
////        Log.i(
////            "shubham",
////            "count:${counter} itemPositionToReturn:$itemPositionToReturn itemPosition:$itemPosition"
////        )
//        if (itemPositionToReturn < 0) return UiModel.Header(categoryList[counter].name)
//        return UiModel.Item(categoryList[counter].itemList[itemPositionToReturn] as Sticker)
//    }

//    fun getSize():Int{
//        var total=0
//        for(category in categoryList)
//            total += (category.itemList.size + 1)
//        return total
//    }

    override fun contains(element: Any): Boolean {
        return listWrapper.contains(element)
    }

    override fun containsAll(elements: Collection<Any>): Boolean {
        return listWrapper.containsAll(elements)
    }

    override fun get(index: Int): Any {
        return listWrapper[index]
    }

    override fun indexOf(element: Any): Int {
        return listWrapper.indexOf(element)
    }

    override fun isEmpty(): Boolean {
        return listWrapper.isEmpty()
    }

    override fun iterator(): Iterator<Any> {
        return listWrapper.iterator()
    }

    override fun lastIndexOf(element: Any): Int {
        return listWrapper.lastIndexOf(element)
    }

    override fun listIterator(): ListIterator<Any> {
        return listWrapper.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<Any> {
        return listWrapper.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Any> {
        return listWrapper.subList(fromIndex, toIndex)
    }
}