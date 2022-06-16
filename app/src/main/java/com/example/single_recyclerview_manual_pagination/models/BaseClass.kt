package com.example.single_recyclerview_manual_pagination.models


abstract class Wrapper<T>() {
    abstract var listOfItems: List<CategoryInheritingAbstractClass<T>>
    abstract var listOfBanner: List<Banner>
}

class BaseClass<T>(var listOfItems: List<CategoryInheritingAbstractClass<T>>) : List<Any> {
    var uiModelList = listOf<UiModel<T>>()
    override val size: Int = uiModelList.size

    fun convertToUiModel(): List<UiModel<T>> {
        val tempUiModelList = mutableListOf<UiModel<T>>()
        for ((i, item) in listOfItems.withIndex()) {
            tempUiModelList.add(UiModel.Header(item.name))
            for ((j, it) in item.itemInheritingAbstractClassList.withIndex()) {
                it.category = item
                tempUiModelList.add(UiModel.Item(it))
            }
            tempUiModelList.add(
                UiModel.LoadMore(
                    itemInheritingAbstractClassAbove = item.itemInheritingAbstractClassList.last(),
                    id = item.id,
                    visible = item.isViewMoreVisible
                )
            )
        }
        return tempUiModelList
    }
//    fun submitList(list: Wrapper<T>, itemType: Item_type) {
//        val modelList = mutableListOf<UiModel<T>>()
//        val wrapper = mutableListOf<Any>()
//        for ((i, items) in list.listOfItems.withIndex()) {
//            if (itemType == Item_type.BANNER && i < list.listOfBanner.size) {
//                modelList.add(UiModel.Banner(list.listOfBanner[i].url))
//                wrapper.add(list.listOfBanner[i])
//            }
//            modelList.add(UiModel.Header(items.name))
//            wrapper.add(items)
//            for (item in items.itemList) {
//                modelList.add(UiModel.Item(item))
//                wrapper.add(item)
//            }
//            modelList.add(
//                UiModel.LoadMore(
//                    itemAbove = items.itemList.last(),
//                    id = items.id,
//                    visible = items.isViewMoreVisible
//                )
//            )
//        }
//        listWrapper = wrapper
//        mainList = modelList
//    }


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