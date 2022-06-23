package com.example.single_recyclerview_manual_pagination.exposed

abstract class Category<T> {
    abstract val id: Int?
    abstract var isViewMoreVisible: Boolean
    abstract var initialCount: Int
    abstract var baseModelOfItemList: List<BaseModelOfItem<T>>
    abstract var currentCount: Int
    abstract var itemsToLoadAfterViewMore: Int
    abstract var name: String
}