package com.example.single_recyclerview_manual_pagination.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.single_recyclerview_manual_pagination.Network.NetworkLayer
import com.example.single_recyclerview_manual_pagination.exposed.State
import com.example.single_recyclerview_manual_pagination.models.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger

class Repository private constructor() {

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Repository().also { instance = it }
        }

        private val count = AtomicInteger(0)

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
        listOfCategory.value!!.removeIf { it.id == -1 }
        listOfCategory.postValue(listOfCategory.value!!)
        Log.i("shubham", "Testing")
        return category
    }

    suspend fun getStickers(
        id: Int,
        offset: String?,
        limit: Int?
    ): List<Sticker>? {
        var listOfT: List<Sticker>? = null
        withContext(Dispatchers.IO) {
            val tempBaseModelItemList =
                mutableListOf<BaseModelOfItemInheritingAbstractClass>()
            try {
//                delay(3000)
                val res = NetworkLayer.retrofitService.getStickers(
                    id = id,
                    limit = limit,
                    offset = offset
                )
                if (id == 405 && count.getAndIncrement() == 0) throw(Exception())
                listOfT = res.items

            } catch (exception: Exception) {
                Log.i("repository", "Exception:$exception")
            }
        }
        return listOfT
    }

    //    lateinit var res:Stickers
    suspend fun getStickersWithOffset(id: Int, offset: String?, limit: Int?): Stickers {
        var res = Stickers(emptyList(), "1")
        try {
//            if(id==405)throw(Exception())
            res = NetworkLayer.retrofitService.getStickers(id = id, limit = limit, offset = offset)
        } catch (exception: Exception) {
            res = Stickers(emptyList(), "error")
            Log.i("repository", "$exception res:${res}")
        }

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