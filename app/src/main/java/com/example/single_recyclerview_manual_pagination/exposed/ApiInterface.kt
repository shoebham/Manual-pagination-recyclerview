package com.example.single_recyclerview_manual_pagination.exposed

interface ApiInterface<T> {
    suspend fun getItemsWithOffset(id: Int, offset: String, limit: Int): List<T>?
}