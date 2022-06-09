package com.example.single_recyclerview_manual_pagination.models

import android.util.Log


class BaseClass(var categoryList: List<Category>) {

    init {
//        for(category in categoryList){
//            category.currentCount+=1
//            if(category.isViewMoreVisible)category.currentCount+=1
//        }
    }

    var currentCategory: Category = categoryList[0]
    var counter = 0
    fun getCategory(): Category {
        Log.i("shubham", "counter$counter")
        return categoryList[counter]
    }
//    fun getCurrentCategory():Category{
//        return currentCategory
//    }

    fun getAt(itemPosition: Int): Int {
//        if(itemPosition==0)return 0
        var i = 0
        var totalTillNow = categoryList[i].itemList.size + 1
        var itemPositionToReturn = itemPosition
        var curr: Category = currentCategory
        while (i < categoryList.size - 1 && itemPositionToReturn >= totalTillNow) {
            itemPositionToReturn = itemPositionToReturn - totalTillNow
            i++;
            totalTillNow = (categoryList[i].itemList.size + 1)
//            currentCategory=categoryList[i]
        }
        itemPositionToReturn -= 1
        counter = i
//        if(itemPositionToReturn==0) header
//        if(itemPositionToReturn==-1) loadmore
//        currentCategory=categoryList[i]
        Log.i(
            "shubham",
            "count:${counter} itemPositionToReturn:$itemPositionToReturn itemPosition:$itemPosition"
        )
        return itemPositionToReturn
    }

    fun getSize():Int{
        var total=0
        for(category in categoryList)
            total += (category.itemList.size + 1)
        return total
    }
}