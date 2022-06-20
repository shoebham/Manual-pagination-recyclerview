package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.BaseModelOfItem
import com.example.single_recyclerview_manual_pagination.exposed.Category


data class CategoryInheritingAbstractClass(
    override val id: Int? = null,
    override var isViewMoreVisible: Boolean = true,
    override var initialCount: Int = 20,
    override var baseModelOfItemList: List<BaseModelOfItem<Sticker>> =
        MutableList(initialCount) { BaseModelOfItemInheritingAbstractClass() },
    override var itemsToLoadAfterViewMore: Int = 20,
    override var name: String = "Loading",
    override var currentCount: Int = baseModelOfItemList.size,
) : Category<Sticker>()
