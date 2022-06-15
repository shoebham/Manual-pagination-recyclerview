package com.example.***REMOVED***_vertical_scroll_stickers.viewModel

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.single_recyclerview_manual_pagination.listContainer
import com.example.single_recyclerview_manual_pagination.models.*
import com.example.single_recyclerview_manual_pagination.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
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

    suspend fun getStickersInitially(): Flow<List<UiModel<Sticker>>> {
        return flow {
            val tempCategory = mutableListOf<Category<Sticker>>()


            for (item in repository.listOfCategory.value!!) {
//                uiModelList.add(UiModel.Header(item.name))
                repository.getStickers(item.id!!, "1", item.initialCount).collectLatest {
                    item.itemList = it
//                    for(i in it) {
//                        i.category = item
//                        uiModelList.add(UiModel.Item(i))
//                    }
//                    tempCategory.add(
//                        Category(
//                            id = item.id,
//                            name = item.name,
//                            itemList = it,
//                            initialCount = 20,
//                            currentCount = it.size,
//                            total = item.total
//                        )
//                    )
                }
            }
            val uiModelList = convertToUiModel(repository.listOfCategory.value!!)
            val listContainer = listContainer<Sticker>()
            listContainer.listOfItems = tempCategory
//            baseClass.submitList(listContainer, BaseClass.Item_type.ITEM)
            emit(uiModelList)
        }
    }

    fun getStickersWithOffset(id: Int, offset: String?, limit: Int?): Flow<List<UiModel<Sticker>>> {
        return flow {
            val tempCategory = mutableListOf<Category<Sticker>>()
//            for (item in repository.listOfCategory.value!!) {
//                uiModelList.add(UiModel.Header(item.name))
            repository.getStickers(id, offset, limit)
                .collectLatest { it ->
                    repository.listOfCategory.value!!.find { it.id == id }?.itemList = it
////                        for(i in it) {
////                            i.category = item
////                            uiModelList.add(UiModel.Item(i))
////                        }
////                        Category(
////                            id = item.id,
////                            name = item.name,
////                            itemList = it,
////                            initialCount = 20,
////                            currentCount = it.size,
////                            total = item.total
////                        )
                }
//            }

            val uiModelList = convertToUiModel(repository.listOfCategory.value!!)
//            val listContainer = listContainer<Sticker>()
//            listContainer.listOfItems = tempCategory
            emit(uiModelList)
        }
    }

    fun convertToUiModel(categoryList: List<Category<Sticker>>): List<UiModel<Sticker>> {
        val uiModelList = mutableListOf<UiModel<Sticker>>()
        for ((i, item) in categoryList.withIndex()) {
            uiModelList.add(UiModel.Header(item.name))
            for ((j, it) in item.itemList.withIndex()) {
                it.category = item
                uiModelList.add(UiModel.Item(it))
            }
            uiModelList.add(
                UiModel.LoadMore(
                    itemAbove = item.itemList.last(),
                    id = item.id,
                    visible = item.isViewMoreVisible
                )
            )
        }
        return uiModelList
    }


//    suspend fun getStickersWithOffset(id: Int, offset: String?, limit: Int?):Stickers{
//        val stickers=repository.getStickersWithOffset(id,offset,limit)
//        convertToBaseModel(stickers)
//    }

//    fun convertToBaseModel(stickers: Stickers): List<BaseModelOfItem<Sticker>> {
//        val baseModelList = mutableListOf<BaseModelOfItem<Sticker>>()
//        stickers.items.forEach {
//            baseModelList.add(BaseModelOfItem(it))
//        }
//        return baseModelList
//    }


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