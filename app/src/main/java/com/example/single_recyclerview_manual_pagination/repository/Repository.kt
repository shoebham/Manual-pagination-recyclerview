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
    val tempHashMap = MutableLiveData<LinkedHashMap<String, Int>>()
    val listOfCategory = MutableLiveData<MutableList<CategoryInheritingAbstractClass>>()

    fun getlist(): MutableLiveData<MutableList<CategoryInheritingAbstractClass>> {
        return listOfCategory
    }

    init {
        listOfCategory.postValue(MutableList(10) { CategoryInheritingAbstractClass() })
        tempHashMap.postValue(LinkedHashMap<String, Int>())
    }


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
        val categories = category.stickerPacks.map {
            CategoryInheritingAbstractClass(id = it.id, name = it.name, initialCount = 20)
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
    ): Stickers? {
        var listOfT: Stickers? = null
        withContext(Dispatchers.IO) {

            try {

                val res = NetworkLayer.retrofitService.getStickers(
                    id = id,
                    limit = limit,
                    offset = offset
                )

                listOfT = res

            } catch (exception: Exception) {
                Log.i("repository", "Exception:$exception")
            }
        }
        return listOfT
    }


    suspend fun getStickersWithOffset(id: Int, offset: String?, limit: Int?): Stickers {
        var res = Stickers(emptyList(), "1")
        try {
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
            val cnt = NetworkLayer.retrofitService.getStickers(id = c.id)
            Log.i("repository", "$cnt")
            c.total = (cnt.items.size)
            tempHashMap.value!!.put(c.name, cnt.items.size)
            tempHashMap.postValue(tempHashMap.value)
            total += cnt.items.size
        }

        return total
    }


}