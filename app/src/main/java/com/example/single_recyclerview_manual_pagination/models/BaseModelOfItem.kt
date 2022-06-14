package com.example.single_recyclerview_manual_pagination.models


class BaseModelOfItem<T>(
    val item: T? = null,
    val category: String? = null,
    val categoryBasedPosition: Int? = null,
    val continuousPosition: Int? = null,
    val isLastItem: Boolean = false
)