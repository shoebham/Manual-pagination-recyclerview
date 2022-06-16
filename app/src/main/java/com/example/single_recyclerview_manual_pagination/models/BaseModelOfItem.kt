package com.example.single_recyclerview_manual_pagination.models


class BaseModelOfItem<T>(
    val item: T? = null,
    var category: Category<T>? = null,
    val categoryBasedPosition: Int? = 0,
    val continuousPosition: Int? = 0,
    var isLastItem: Boolean = false,
    var state: State = State.NOT_LOADING,
    var isLoadMoreClicked: Boolean = false
)