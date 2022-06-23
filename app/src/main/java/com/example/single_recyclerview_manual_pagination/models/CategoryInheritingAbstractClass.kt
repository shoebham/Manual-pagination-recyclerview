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
    override var currentCount: Int = baseModelOfItemList.size,
    var name: String = "Loading",
) : Category<Sticker>()

//things to do
// val var
// docs
// nomenclature


//things done
// !!
// dummy data
// keep only necessary things
// a4 size line
// warnings
// coroutine scope

