package com.example.single_recyclerview_manual_pagination.exposed


abstract class UiModel<T> {
    class Item<T>(val baseModelOfItem: BaseModelOfItem<T>) : UiModel<T>()
    class Header<T>(val category: Category<T>) : UiModel<T>()
    class LoadMore<T>(
        val id: Int? = null,
        val baseModelItemAbove: BaseModelOfItem<T>,
        var visible: Boolean = true
    ) : UiModel<T>()
}
