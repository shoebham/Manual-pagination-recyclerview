package com.example.single_recyclerview_manual_pagination.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.single_recyclerview_manual_pagination.Network.NetworkLayer
import com.example.single_recyclerview_manual_pagination.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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
val tempHashMap = MutableLiveData<LinkedHashMap<String, Int>>()
    val listOfCategory = MutableLiveData<MutableList<CategoryInheritingAbstractClass>>()

    fun getlist(): MutableLiveData<MutableList<CategoryInheritingAbstractClass>> {
        return listOfCategory
    }

    init {
        listOfCategory.postValue(MutableList(10) { CategoryInheritingAbstractClass() })
        tempHashMap.postValue(LinkedHashMap<String, Int>())
    }
//    private var _tabPosition = MutableLiveData<Int>()
//    val tabPosition = _tabPosition


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getStickerPacks(): StickerPacks {
        val category = NetworkLayer.retrofitService.getStickerPacks()
        for ((i, cat) in category.stickerPacks.withIndex()) {
            listOfCategory.value!![i] = (
                    CategoryInheritingAbstractClass(
                        id = cat.id,
                        name = cat.name,
                        isViewMoreVisible = i < category.stickerPacks.size - 1,
                        initialCount = 20,
                    )
                    )
        }
        listOfCategory.value!!.removeIf { it.id == null }
        listOfCategory.postValue(listOfCategory.value!!)

        Log.i("shubham", "Testing")
        return category
    }

    suspend fun getStickers(
        id: Int,
        offset: String?,
        limit: Int?
    ): Flow<List<BaseModelOfItemInheritingAbstractClass>> {
        return flow {
            val tempBaseModelItemList =
                mutableListOf<BaseModelOfItemInheritingAbstractClass>()
//            for(item in list){
            val res = getStickersWithOffset(id, offset, limit)
            for ((i, item) in res.items.withIndex()) {
                tempBaseModelItemList.add(
                    BaseModelOfItemInheritingAbstractClass(
                        item,
                        categoryBasedPosition = offset!!.toInt() + i,
                        state = State.LOADED,
                        isLastItem = res.items.size < limit!!
                    )
                )
            }
//            res.items.forEach { tempBaseModelItemList.add(BaseModelOfItem(it)) }
//            }
            emit(tempBaseModelItemList)
        }

//        return itemList.items
    }

    suspend fun getStickersWithOffset(id: Int, offset: String?, limit: Int?): Stickers {
        val res = NetworkLayer.retrofitService.getStickers(id = id, limit = limit, offset = offset)

//
//        listOfCategory.value!!.find {it.id==id }?.itemList = tempBaseModelItemList
//        listOfCategory.value!!.find {it.id==id }?.currentCount = res.items.size
//        listOfCategory.value!!.find {it.id==id }?.initialCount = res.items.size
//        listOfCategory.postValue(listOfCategory.value!!)
//        Log.i("repository", "${listOfCategory.value}")
        return res
    }

    suspend fun getCount(category: StickerPacks): Int {
        var total = 0
        for ((i, c) in category.stickerPacks.withIndex()) {
//            if(c.id==405)continue
            val cnt = NetworkLayer.retrofitService.getStickers(id = c.id)
//            delay(3000)

            Log.i("repository", "$cnt")

            c.total = (cnt.items.size)
            tempHashMap.value!!.put(c.name, cnt.items.size)
            tempHashMap.postValue(tempHashMap.value)
            total += cnt.items.size

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

//    fun getListOfCategory():List<Category>{
//        return listOfCategory
//    }
//    init {
//        getStickerPacks()
//        getCount(stickerPacks.value)
//    }

}