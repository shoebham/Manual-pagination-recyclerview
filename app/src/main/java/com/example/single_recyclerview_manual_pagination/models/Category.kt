package com.example.single_recyclerview_manual_pagination.models

data class Category<T>(

    val id: Int? = null,
    val name: String = "Loading",
    var isViewMoreVisible: Boolean = true,
    var initialCount: Int = 20,
    var itemList: List<BaseModelOfItem<T>> = MutableList(initialCount) {
        BaseModelOfItem()
    },
    var currentCount: Int = itemList.size,
    var total: Int = 0
)