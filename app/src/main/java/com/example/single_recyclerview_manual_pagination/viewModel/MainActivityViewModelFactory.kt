package com.example.single_recyclerview_manual_pagination.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.single_recyclerview_manual_pagination.repository.Repository

class MainActivityViewModelFactory(
    private val repository: Repository,
    private val application: Application
) : ViewModelProvider.NewInstanceFactory() {

    /**
     * creates a view model factory to provide access to application context
     *
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainActivityViewModel(
            repository,
            application
        ) as T
    }
}