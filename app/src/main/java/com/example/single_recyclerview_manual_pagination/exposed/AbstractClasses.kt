package com.example.single_recyclerview_manual_pagination.exposed

import com.example.single_recyclerview_manual_pagination.models.BaseModelOfItemInheritingAbstractClass
import com.example.single_recyclerview_manual_pagination.models.State

abstract class Category<T> {
    abstract val id: Int?
    abstract var isViewMoreVisible: Boolean
    abstract var initialCount: Int
    abstract var baseModelOfItemList: List<BaseModelOfItem<T>>
    abstract var currentCount: Int
    abstract var itemsToLoadAfterViewMore: Int
    abstract var name: String
}

abstract class BaseModelOfItem<T> {
    abstract val item: T?
    abstract var category: Category<T>?
    abstract val categoryBasedPosition: Int
    abstract val continuousPosition: Int
    abstract var isLastItem: Boolean
    abstract var state: State
    abstract var isLoadMoreClicked: Boolean
}

abstract class Banner {
}

data class ListWrapper<T>(
    val categoryList: List<Category<T>>,
    val bannerList: List<Banner>
)

