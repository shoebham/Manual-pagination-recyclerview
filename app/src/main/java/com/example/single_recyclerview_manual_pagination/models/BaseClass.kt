package com.example.single_recyclerview_manual_pagination.models


class BaseClass(private var categoryList:List<Category<Sticker>>) {

    init {
        for(category in categoryList){
            category.currentCount+=1
            if(category.isViewMoreVisible)category.currentCount+=1
        }
    }

    fun getAt(itemPosition:Int): Int {
        var i=0
        var totalTillNow=categoryList[i].currentCount
        var itemPositionToReturn=0
        while(itemPosition>totalTillNow){
            itemPositionToReturn = itemPosition-totalTillNow
            totalTillNow = categoryList[i++].currentCount
            i++;
        }
        itemPositionToReturn-=1
//        if(itemPositionToReturn==0) header
//        if(itemPositionToReturn==-1) loadmore

        if(itemPositionToReturn==-1){
            itemPositionToReturn= -1*(categoryList[i].currentCount)
        }
        if(itemPositionToReturn==0){
            itemPositionToReturn=0
        }
        return itemPositionToReturn
    }

    fun getSize():Int{
        var total=0
        for(category in categoryList)
            total+=category.itemList.size
        return total
    }
}