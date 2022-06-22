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

open class BaseModelOfItem<T>(
    open val item: T? = null,
    open var category: Category<T>? = null,
    open val categoryBasedPosition: Int = 0,
    open val continuousPosition: Int = 0,
    open var isLastItem: Boolean = false,
    open var state: State = State.NOT_LOADING,
    open var isLoadMoreClicked: Boolean = false
)


abstract class Banner {
}

data class ListWrapper<T>(
    val categoryList: List<Category<T>>,
    val bannerList: List<Banner>
)

