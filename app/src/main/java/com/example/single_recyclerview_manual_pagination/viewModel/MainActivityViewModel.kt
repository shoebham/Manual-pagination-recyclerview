package com.example.***REMOVED***_vertical_scroll_stickers.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.single_recyclerview_manual_pagination.repository.Repository
import java.util.concurrent.atomic.AtomicInteger

class MainActivityViewModel(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {
    companion object {
        private val count = AtomicInteger(0)
    }

    private val toggle: Boolean = true

    fun getCategories() = repository.stickerPacks
    fun getCount() = repository.count
    fun getIndividualCount() = repository.individualCount
    fun getTabPosition() = repository.tabPosition
    fun getCategoryList() = repository.categoryList


}