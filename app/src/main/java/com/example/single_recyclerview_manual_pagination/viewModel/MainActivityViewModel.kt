package com.example.***REMOVED***_vertical_scroll_stickers.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.single_recyclerview_manual_pagination.models.Category
import com.example.single_recyclerview_manual_pagination.models.Sticker
import com.example.single_recyclerview_manual_pagination.models.StickerPacks
import com.example.single_recyclerview_manual_pagination.repository.Repository
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

class MainActivityViewModel(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {
    companion object {
        private val count = AtomicInteger(0)
    }


    private val toggle: Boolean = true


    private var _stickerPacks = MutableLiveData<StickerPacks>()

    @RequiresApi(Build.VERSION_CODES.N)
    val stickerPacks: LiveData<StickerPacks> = liveData {
        emit(repository.getStickerPacks())
    }

    private var _categoryList = MutableLiveData<List<Category<Sticker>>>()
    val categoryList: MutableLiveData<MutableList<Category<Sticker>>> = (repository.getlist())

    private var _count = MutableLiveData<Int>()
    val count: LiveData<Int> = liveData {
        emit(repository.getCount(stickerPacks.value!!))
    }

    private var _individualCount = MutableLiveData<LinkedHashMap<String, Int>>()
    val individualCount: LiveData<LinkedHashMap<String, Int>> = repository.tempHashMap

    val tempHashMap = LinkedHashMap<String, Int>()
    private var _tabPosition = MutableLiveData<Int>()
    val tabPosition = _tabPosition


//    suspend fun getCategories(){
//        stickerPacks.postValue(repository.getStickerPacks())
//    }
//    suspend fun getCount(){
//        count.postValue(repository.getCount(stickerPacks.value!!))
//    }
//    fun getIndividualCount(){
//        individualCount.postValue(repository.tempHashMap)
//    }
//    fun getTabPosition() = repository.tabPosition
//    suspend fun getCategoryList() = categoryList.postValue(repository.mapResponse(stickerPacks.value!!))


}