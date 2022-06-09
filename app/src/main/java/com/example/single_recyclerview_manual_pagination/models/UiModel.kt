package com.example.single_recyclerview_manual_pagination.models

sealed class StickerUiModel {
    class StickerItem(val sticker: Sticker) : StickerUiModel()
    class StickerHeader(val text: String) : StickerUiModel()
}