package com.example.single_recyclerview_manual_pagination.models

import android.util.Log


class BaseClass<T>(var categoryList: List<Category<T>>) {

    var currentCategory: Category<T> = categoryList[0]
    var counter = 0
    fun getCategory(): Category<T> {
        Log.i("shubham", "counter$counter")
        return categoryList[counter]
    }

    fun getAt(itemPosition: Int): UiModel {
//        if(itemPosition==0)return 0
        var i = 0
        var totalTillNow = categoryList[i].itemList.size + 1
        var itemPositionToReturn = itemPosition
        while (i < categoryList.size - 1 && itemPositionToReturn >= totalTillNow) {
            itemPositionToReturn -= totalTillNow
            i++;
            totalTillNow = (categoryList[i].itemList.size + 1)
//            currentCategory=categoryList[i]
        }
        itemPositionToReturn -= 1
        counter = i
//        if(itemPositionToReturn==0) header
//        if(itemPositionToReturn==-1) loadmore
//        currentCategory=categoryList[i]
//        Log.i(
//            "shubham",
//            "count:${counter} itemPositionToReturn:$itemPositionToReturn itemPosition:$itemPosition"
//        )
        if (itemPositionToReturn < 0) return UiModel.Header(categoryList[counter].name)
        return UiModel.Item(categoryList[counter].itemList[itemPositionToReturn])
    }

    fun getSize():Int{
        var total=0
        for(category in categoryList)
            total += (category.itemList.size + 1)
        return total
    }
}