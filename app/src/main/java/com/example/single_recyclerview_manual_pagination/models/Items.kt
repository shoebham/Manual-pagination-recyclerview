package com.example.single_recyclerview_manual_pagination.models

data class Items<T>(
    val items:List<T> = emptyList()
// if load more clicked add empty sticker to the list
)