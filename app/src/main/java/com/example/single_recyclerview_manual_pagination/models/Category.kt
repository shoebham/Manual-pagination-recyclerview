package com.example.single_recyclerview_manual_pagination.models


data class Category<T>(
    val id:Int,
    val name:String,
    var isViewMoreVisible:Boolean,
    var initialCount:Int,
    val itemList:List<T>,
    var currentCount:Int = itemList.size
)