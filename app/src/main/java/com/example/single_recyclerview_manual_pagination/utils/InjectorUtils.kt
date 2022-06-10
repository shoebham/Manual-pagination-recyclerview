package com.example.***REMOVED***_vertical_scroll_stickers.utils

import android.app.Application
import com.example.***REMOVED***_vertical_scroll_stickers.viewModel.MainActivityViewModelFactory
import com.example.single_recyclerview_manual_pagination.repository.Repository

object InjectorUtils {
    /**
     * Provides View Model Factory which provides access to repository instance and application
     */
    fun provideMainActivityViewModelFactory(application: Application): MainActivityViewModelFactory {
        val repository = Repository.getInstance()
        return MainActivityViewModelFactory(repository, application)
    }
}