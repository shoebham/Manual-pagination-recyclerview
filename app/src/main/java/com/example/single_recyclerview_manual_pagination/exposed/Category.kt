package com.example.single_recyclerview_manual_pagination.exposed


abstract class Category<T> {
    abstract val id: Int
    abstract val isViewMoreVisible: Boolean
    abstract val currentCount: Int
    abstract val itemsToLoadAfterViewMore: Int
    abstract val initialCount: Int
    abstract var baseModelOfItemList: List<BaseModelOfItem<T>>
}