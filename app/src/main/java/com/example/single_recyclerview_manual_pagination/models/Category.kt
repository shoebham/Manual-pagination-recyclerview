package com.example.single_recyclerview_manual_pagination.models

data class Category(
    val id: Int,
    val name: String,
    var isViewMoreVisible: Boolean,
    var initialCount: Int,
    val itemList: List<Sticker>,
    var currentCount: Int = itemList.size
)