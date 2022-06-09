package com.example.single_recyclerview_manual_pagination.repository

import android.provider.SyncStateContract
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.single_recyclerview_manual_pagination.Network.NetworkLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Wrapper

class Repository private constructor() {

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Repository().also { instance = it }
        }
    }

    private var _categories = MutableLiveData<StickerPacks>()
    val categories = _categories
    private var _count = MutableLiveData<Int>()
    val count = _count
    private var _individualCount = MutableLiveData<LinkedHashMap<String, Int>>()
    val individualCount = _individualCount

    val tempHashMap = LinkedHashMap<String, Int>()
    private var _tabPosition = MutableLiveData<Int>()
    val tabPosition = _tabPosition

    fun getStickerPacks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val category = NetworkLayer.retrofitService.getStickerPacks()
//                Log.i("repostiory", "${category.stickerPacks.size}")
                category.stickerPacks.removeIf { it.id == 405 }

                getCount(category)
                categories.postValue(category)
            } catch (e: Exception) {
                Log.i("shubham", "Exception $e")
            }
        }
    }

    suspend fun getCount(category: StickerPacks) {
        var total = 0
        for (c in category.stickerPacks) {
//            if(c.id==405)continue
            val cnt = NetworkLayer.retrofitService.getStickers(id = c.id)
            Log.i("repository", "$cnt")
            c.total = (cnt.items.size - 1)
            tempHashMap.put(c.name!!, cnt.items.size - 1)
            total += cnt.items.size
        }
        individualCount.postValue(tempHashMap)
        count.postValue(total)
    }

    init {
        getStickerPacks()
    }

}