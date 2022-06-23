package com.example.single_recyclerview_manual_pagination.exposed


open class BaseModelOfItem<T>(
    open val item: T? = null,
    open val categoryBasedPosition: Int = 0,
    open val continuousPosition: Int = 0,
    open var isLastItem: Boolean = false,
    open var category: Category<T>? = null,
    open var state: State = State.NOT_LOADING,
    open var isLoadMoreClicked: Boolean = false
)


