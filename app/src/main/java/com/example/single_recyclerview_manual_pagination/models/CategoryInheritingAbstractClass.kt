package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.Category


data class CategoryInheritingAbstractClass<T>(
    override val id: Int? = null,
    override var isViewMoreVisible: Boolean = true,
    override var initialCount: Int = 20,
    override var itemInheritingAbstractClassList: List<BaseModelOfItemInheritingAbstractClass<T>> =
        MutableList(initialCount) { BaseModelOfItemInheritingAbstractClass() },
    override var itemsToLoadAfterViewMore: Int = 5,
    override var name: String = "Loading",
    override var currentCount: Int = itemInheritingAbstractClassList.size,
) : Category<T>()
