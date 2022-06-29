package com.example.single_recyclerview_manual_pagination.models

import com.example.single_recyclerview_manual_pagination.exposed.BaseModelOfItem
import com.example.single_recyclerview_manual_pagination.exposed.Category


data class CategoryInheritingAbstractClass(
    var name: String = "Loading",
    override val id: Int = -1,
    override val isViewMoreVisible: Boolean = true,
    override val initialCount: Int = 20,
    override val itemsToLoadAfterViewMore: Int = 20,
) : Category<Sticker>(initialCount)


//things to do
// val var

//things done
// !!
// dummy data
// keep only necessary things
// a4 size line
// warnings
// coroutine scope
// nomenclature
// docs
