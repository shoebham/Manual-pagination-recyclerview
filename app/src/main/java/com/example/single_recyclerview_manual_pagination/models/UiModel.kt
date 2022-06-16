package com.example.single_recyclerview_manual_pagination.models


sealed class UiModel<T>() {
    class Item<T>(val baseModelOfItem: BaseModelOfItem<T>) : UiModel<T>()
    class Header<T>(val text: String?) : UiModel<T>()
    class Banner<T>(val url: String) : UiModel<T>()
    class LoadMore<T>(
        val id: Int? = null,
        val itemAbove: BaseModelOfItem<T>? = null,
        var visible: Boolean = true
    ) : UiModel<T>()
}