package com.example.single_recyclerview_manual_pagination.models


import com.example.single_recyclerview_manual_pagination.exposed.BaseModelOfItem
import com.example.single_recyclerview_manual_pagination.exposed.Category


data class BaseModelOfItemInheritingAbstractClass<T>(
    override val item: T? = null,
    override var category: Category<T>? = null,
    override val categoryBasedPosition: Int = 0,
    override val continuousPosition: Int = 0,
    override var isLastItem: Boolean = false,
    override var state: State = State.NOT_LOADING,
    override var isLoadMoreClicked: Boolean = false
) : BaseModelOfItem<T>()