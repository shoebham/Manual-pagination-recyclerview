package com.example.single_recyclerview_manual_pagination.models

sealed class UiModel {
    class Item<T>(val item: T?) : UiModel()
    class Header(val text: String?) : UiModel()
}