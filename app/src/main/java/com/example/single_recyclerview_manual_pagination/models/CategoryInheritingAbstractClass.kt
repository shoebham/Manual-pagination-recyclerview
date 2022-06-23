package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.BaseModelOfItem
import com.example.single_recyclerview_manual_pagination.exposed.Category


data class CategoryInheritingAbstractClass(
    override val id: Int = -1,
    override var isViewMoreVisible: Boolean = true,
    override var initialCount: Int = 20,
    override var baseModelOfItemList: List<BaseModelOfItem<Sticker>> =
        MutableList(initialCount) { BaseModelOfItemInheritingAbstractClass() },
    override var itemsToLoadAfterViewMore: Int = 20,
    override var name: String = "Loading",
    override var currentCount: Int = baseModelOfItemList.size,
) : Category<Sticker>()

// dummy data
// val var
// docs
// !!
// keep only necessary things
// nomenclature
// warnings
// a4 size line
// coroutine scope