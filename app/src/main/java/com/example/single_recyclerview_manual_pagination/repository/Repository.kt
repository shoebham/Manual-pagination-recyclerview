package com.example.single_recyclerview_manual_pagination.repository

import android.os.Build
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.single_recyclerview_manual_pagination.Network.NetworkLayer
import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItem
import com.example.single_recyclerview_manual_pagination.models.Category
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
val tempHashMap = MutableLiveData<LinkedHashMap<String, Int>>()
    val listOfCategory = MutableLiveData<MutableList<Category<Sticker>>>()

    fun getlist(): MutableLiveData<MutableList<Category<Sticker>>> {

        return listOfCategory
    }

    init {
        listOfCategory.postValue(MutableList(10) { Category() })
        tempHashMap.postValue(LinkedHashMap<String, Int>())
    }
//    private var _tabPosition = MutableLiveData<Int>()
//    val tabPosition = _tabPosition


    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun getStickerPacks(): StickerPacks {
        val category = NetworkLayer.retrofitService.getStickerPacks()
        for ((i, cat) in category.stickerPacks.withIndex()) {
            listOfCategory.value!![i] = (
                    Category(
                        id = cat.id,
                        name = cat.name,
                        isViewMoreVisible = i < category.stickerPacks.size - 1,
                        initialCount = 20
                    )
                    )
        }
        listOfCategory.value!!.removeIf { it.id == null }

        return category
    }

    suspend fun getStickers(id: Int): List<Sticker> {
        val itemList = NetworkLayer.retrofitService.getStickers(id = id)
        return itemList.items
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

            val tempBaseModelItemList = mutableListOf<BaseModelOfItem<Sticker>>()
            cnt.items.forEach { tempBaseModelItemList.add(BaseModelOfItem(it)) }

            listOfCategory.value!![i].itemList = tempBaseModelItemList
            listOfCategory.value!![i].currentCount = cnt.items.size
            listOfCategory.value!![i].initialCount = cnt.items.size
            listOfCategory.postValue(listOfCategory.value!!)
            Log.i("repository", "${listOfCategory.value}")
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