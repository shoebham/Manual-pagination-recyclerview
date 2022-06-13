package com.example.single_recyclerview_manual_pagination.models


sealed class UiModel {
    class Item(val item: Sticker?) : UiModel()
    class Header(val text: String?) : UiModel()
}