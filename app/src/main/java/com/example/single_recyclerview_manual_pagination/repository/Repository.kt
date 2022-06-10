package com.example.single_recyclerview_manual_pagination.repository

import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.single_recyclerview_manual_pagination.Network.NetworkLayer
import com.example.single_recyclerview_manual_pagination.models.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Repository private constructor() {

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Repository().also { instance = it }
        }
    }

//    private var _stickerPacks = MutableLiveData<StickerPacks>()
//    val stickerPacks = _stickerPacks
//
//    private var _categoryList = MutableLiveData<List<Category>>()
//    val categoryList = _categoryList
//
//    private var _count = MutableLiveData<Int>()
//    val count = _count
//    private var _individualCount = MutableLiveData<LinkedHashMap<String, Int>>()
//    val individualCount = _individualCount
//
val tempHashMap = LinkedHashMap<String, Int>()
    val listOfCategory = ArrayList<Category>(10)

//    private var _tabPosition = MutableLiveData<Int>()
//    val tabPosition = _tabPosition


    suspend fun getStickerPacks(): StickerPacks {
        val category = NetworkLayer.retrofitService.getStickerPacks()
        for (cat in category.stickerPacks) {
            listOfCategory.add(
                Category(
                    id = cat.id,
                    name = cat.name!!,
                    isViewMoreVisible = false,
                    initialCount = category.stickerPacks.size,
                )
            )
        }
//                Log.i("repostiory", "${category.stickerPacks.size}")
//                category.stickerPacks.removeIf { it.id == 405 }
//                stickerPacks.postValue(category)
        return category
    }

//    suspend fun mapResponse(category: StickerPacks):List<Category> {
////        val c =BaseClass()
////        for (cat in category.stickerPacks) {
////            listOfCategory.add(
////                Category(
////                    id = cat.id,
////                    name = cat.name!!,
////                    isViewMoreVisible = false,
////                    initialCount = category.stickerPacks.size,
////                    itemList = getStickers(cat.id)
////                )
////            )
////        }
//        return listOfCategory
////        categoryList.postValue(listOfCategory)
//    }

    suspend fun getStickers(id: Int): List<Sticker> {
        val itemList = NetworkLayer.retrofitService.getStickers(id = id)
        return itemList.items
    }

    suspend fun getCount(category: StickerPacks): Int {
        var total = 0
        for ((i, c) in category.stickerPacks.withIndex()) {
//            if(c.id==405)continue
            val cnt = NetworkLayer.retrofitService.getStickers(id = c.id)
            Log.i("repository", "$cnt")
            c.total = (cnt.items.size)
            tempHashMap.put(c.name!!, cnt.items.size)
            total += cnt.items.size
            listOfCategory[i].itemList = cnt.items
//            listOfCategory.add(
//                Category(
//                    id = c.id,
//                    name = c.name!!,
//                    isViewMoreVisible = false,
//                    initialCount = category.stickerPacks.size,
//                    itemList = getStickers(c.id)
//
        }
//        categoryList.postValue(listOfCategory)
//        individualCount.postValue(tempHashMap)
//        count.postValue(total)
        return total
    }

//    init {
//        getStickerPacks()
//        getCount(stickerPacks.value)
//    }

}