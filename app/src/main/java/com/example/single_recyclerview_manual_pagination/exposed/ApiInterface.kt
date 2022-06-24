package com.example.single_recyclerview_manual_pagination.exposed


/**
 * Interface with type T and a function to call the API
 * @param T the type of the response
 */
interface ApiInterface<T> {
    /**
     * Call the API
     * @param id the id for the API call
     * @param offset offset from which to start the call
     * @param limit number of items to return
     * @return List of T where T is a generic item
     */
    suspend fun getItemsWithOffset(id: Int, offset: String, limit: Int): List<T>?
}