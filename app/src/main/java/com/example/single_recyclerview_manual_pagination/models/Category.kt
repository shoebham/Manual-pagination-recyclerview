package com.example.single_recyclerview_manual_pagination.models

data class Category<T>(
    val id: Int? = null,
    val name: String? = null,
    var isViewMoreVisible: Boolean = false,
    var initialCount: Int = 10,
    var itemList: List<T?> = MutableList(initialCount) {
        null
    },
    var currentCount: Int = itemList.size
)