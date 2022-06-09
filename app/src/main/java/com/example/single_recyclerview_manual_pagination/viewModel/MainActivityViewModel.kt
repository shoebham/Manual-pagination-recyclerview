package com.example.***REMOVED***_vertical_scroll_stickers.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.single_recyclerview_manual_pagination.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

class MainActivityViewModel(
    private val repository: Repository,
    application: Application,
) : AndroidViewModel(application) {
    companion object {
        private val count = AtomicInteger(0)
    }

    private val toggle: Boolean = true

    fun getCategories() = repository.categories
    fun getCount() = repository.count
    fun getIndividualCount() = repository.individualCount
    fun getTabPosition() = repository.tabPosition
//    fun getPageSource() = repository.pageSource


}