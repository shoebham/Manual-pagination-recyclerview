package com.example.single_recyclerview_manual_pagination.models


sealed class UiModel<T>() {
    class Item<T>(val baseModelOfItemInheritingAbstractClass: BaseModelOfItemInheritingAbstractClass<T>) :
        UiModel<T>()

    class Header<T>(val text: String?) : UiModel<T>()
    class Banner<T>(val url: String) : UiModel<T>()
    class LoadMore<T>(
        val id: Int? = null,
        val itemInheritingAbstractClassAbove: BaseModelOfItemInheritingAbstractClass<T>? = null,
        var visible: Boolean = true
    ) : UiModel<T>()
}

